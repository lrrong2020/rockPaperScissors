package Client;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Pages.*;
import Model.*;

public class ClientMain extends Application
{
	// Text area to display contents
	private static TextArea ta = new TextArea();

	public static Client client = null;
	public static boolean hasStarted = false;
	public static boolean hasStopped = false;

	private Integer roundNoInt = Integer.valueOf(1);

	//private Scene findIPPage;
	private Scene welcomePage;
	public static DuringTheGame during=new DuringTheGame();
	public static Parent nodes=during.CreateGamePage();
	private WelcomePage start=new WelcomePage();
	private static ArrayList<EventHandler<MouseEvent>>listeners=null;

	public static boolean exceptionallyStopped = false;
	public static boolean clientExited = false;
	public static AnimationTimer am;
	public static AnimationTimer am1;
	public static AnimationTimer am2;
	public static AnimationTimer am3;

	//private Scene findIPPage;

	public static Scene startWelcomePage;

	public ClientMain(){
	}

	//Create the Welcome Page to show
	public void CreateWelcomePage() {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setPadding(new Insets(10));

		Text enterTxt = new Text("Enter the IP address:");
		enterTxt.setFont(Font.font("Tahoma", FontWeight.LIGHT, 25));
		grid.add(enterTxt, 0, 0);


		TextField IP = new TextField();

		IP.setPromptText("IP address");
		grid.add(IP, 0, 2);


		Button enter = new Button("OK");
		grid.add(enter, 0, 3);
		welcomePage=new Scene(grid,600,400);


		Scene startWelcomePage=new Scene(start.getWelcomePage(),600,400);

		startWelcomePage.getStylesheets().add(getClass().getResource("../CSS/PagesSettings.css").toExternalForm());


		enter.setOnAction(e->{
			//System.out.println(IP.getText().toString().trim());
			String ipAddr=IP.textProperty().get().trim();
			ClientMain.client=new Client(ipAddr);
			//appendTextArea("Client generated");
			try 
			{
				client.initialize();
				//appendTextArea("Client initialized");
			}
			catch(IOException ioe) 
			{
				//appendTextArea("Client initialize failed");
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning!");
				alert.setHeaderText("Connection Failed");
				alert.setContentText("Please restart the game.");
				alert.showAndWait();
				System.exit(1);

			}
			catch (ClassNotFoundException | NullPointerException e1) 
			{
				//Invalid DataBean
				//server passed a null
				e1.printStackTrace();
				//appendTextArea("Invalid Data from server!");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			try {
				client.initSemaphore.acquire();

			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Stage window;
			if(client.getIsHost()) {
				window=(Stage)enter.getScene().getWindow();
				window.setTitle("Welcome to the Rock Paper Scissors Game!");
				window.setScene(startWelcomePage);

				Alert onePlayer = new Alert(AlertType.WARNING);
				onePlayer.setTitle("Warning");
				onePlayer.setHeaderText("You cannot start now!");
				onePlayer.setContentText("The room has only 1 player");

				start.bt1.setOnAction(m->{
					if(!client.isCanStart()) 
					{
						onePlayer.show();
						return;
					}

					Stage window1=(Stage)start.bt1.getScene().getWindow();
					window1.setTitle("Game started");
					try {
						client.hostStartGame(1);


					} catch (InterruptedException e1) {

						e1.printStackTrace();
					}
					setDuringGameScene(window1);



				});
				start.bt2.setOnAction(m->{
					if(!client.isCanStart()) 
					{
						onePlayer.show();
						return;
					}


					Stage window1=(Stage)start.bt2.getScene().getWindow();
					window1.setTitle("Game started");
					try {
						client.hostStartGame(3);


					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					setDuringGameScene(window1);


				});
				start.bt3.setOnAction(m->{
					if(!client.isCanStart()) 
					{
						onePlayer.show();
						return;
					}

					Stage window1=(Stage)start.bt3.getScene().getWindow();
					window1.setTitle("Game started");
					try {
						client.hostStartGame(5);


					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					setDuringGameScene(window1);


				});

				ClientMain.am1=new StartEndChecker(window);

				am1.start();
				ClientMain.am2 = new ExceptionallyStopped(window);
				am2.start();
				
				ClientMain.am3=new ClientExitChecker(window);
				am3.start();
			}
			else {

				WaitingPage waiting=new WaitingPage();
				Scene waitingRes=new Scene(waiting.CreateWaitingPage(),600,400);
				waitingRes.getStylesheets().add(getClass().getResource("../CSS/PagesSettings.css").toExternalForm());
				window=(Stage)enter.getScene().getWindow();
				window.setScene(waitingRes);
				window.setTitle("Game will be started in several seconds");
				ClientMain.am2 = new ExceptionallyStopped(window);
				am2.start();
				ClientMain.am = new StartGameChecker(window);
				am.start();

				ClientMain.am1=new StartEndChecker(window);
				am1.start();
				
				ClientMain.am3=new ClientExitChecker(window);
				am3.start();

			}	
			client.initSemaphore.release();
		});
	}

	//similar syntax for rewriting append method of jTextArea of java.swing
	//use it the same way as //System.out.println(String string) !
//	public static void appendTextArea(String string) 
//	{
//		ta.setText(ta.getText() + "\n" +string);
//		//System.out.println("\n" + string);//debug
//	}

	public static void log(String string) 
	{
		//System.out.println(string);
	}


	//get JavaFX Group


	public static ArrayList<EventHandler<MouseEvent>> getEvent()
	{

		listeners=new ArrayList<>();
		//listen the mouse event and handle the event
		EventHandler<MouseEvent> rockListener = new EventHandler<MouseEvent>() 
		{ 
			@Override 
			public void handle (MouseEvent e)
			{	
				try 
				{
					client.choose(Choice.GESTURES.ROCK);
				}
				catch (ClassNotFoundException e1) 
				{
					e1.printStackTrace();
				}
			}
		};

		//listen the mouse event and handle the event
		EventHandler<MouseEvent> paperListener = new EventHandler<MouseEvent>() 
		{ 
			@Override 
			public void handle (MouseEvent e)
			{	
				try 
				{
					client.choose(Choice.GESTURES.PAPER);
				} 
				catch (ClassNotFoundException e1) 
				{
					e1.printStackTrace();
				}
			}
		};

		//listen the mouse event and handle the event
		EventHandler<MouseEvent> scissorsListener = new EventHandler<MouseEvent>() 
		{ 
			@Override 
			public void handle (MouseEvent e)
			{	
				try 
				{
					client.choose(Choice.GESTURES.SCISSORS);
				} 
				catch (ClassNotFoundException e1) 
				{
					e1.printStackTrace();
				}
			}
		};
		listeners.add(rockListener);
		listeners.add(paperListener);
		listeners.add(scissorsListener);

		return listeners;
	}


	//start JavaFX application
	@Override
	public void start(Stage stage) throws Exception
	{	

		stage.setTitle("Welcome to the Rock Paper Scissors Game!");

		CreateWelcomePage();

		stage.setScene(welcomePage);
		Platform.setImplicitExit(false);
		stage.setOnCloseRequest(event ->{
			event.consume();
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Exit");
			alert.setHeaderText(null);
			alert.setContentText("Do you want to exit?");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK) {
				if(client != null)
				{
					client.clientStop();
				}
				if(am != null)
				{
					am.stop();
				}
				if(am1 != null)
				{
					am1.stop();
				}
				if(am2 != null)
				{
					am2.stop();
				}
				if(am3 != null) 
				{
					am3.stop();
				}
				Platform.exit();
				System.exit(0);
			}
		});

		stage.show();
	}

	//main method to launch JavaFX application
	public static void main(String[] args) 
	{
		launch(args);
	}


	private class StartGameChecker extends AnimationTimer 
	{
		Stage window;

		public StartGameChecker(Stage window)
		{
			this.window = window;
		}

		@Override
		public void handle(long arg0)
		{
			if(!hasStarted) 
			{
				checkStartGame();
			}
			else 
			{
				setDuringGameScene(window);
				stop();
			}
		}
	}
	private void setDuringGameScene(Stage window) {
		Parent root=during.CreateGamePage();
		labelActionPerformed(new ActionEvent(root, 0, null));

		label3ActionPerformed(new ActionEvent(root, 1, null));
		
		Scene duringGame=new Scene(root,600,400);
		duringGame.getStylesheets().add(getClass().getResource("../CSS/GamePageSettings.css").toExternalForm());
		window.setScene(duringGame);
		window.setTitle("Game started");
	}

	private class StartEndChecker extends AnimationTimer 
	{
		Stage window;
		public StartEndChecker(Stage window)
		{
			this.window = window;
		}

		@Override
		public void handle(long arg0)
		{
			if(!hasStopped) 
			{
				checkEndGame();
			}
			else 
			{
				GameOverPage over = new GameOverPage(client);
				Scene overGame = new Scene(over.CreateOverPage(), 600, 400);
				overGame.getStylesheets().add(getClass().getResource("../CSS/GamePageSettings.css").toExternalForm());
				window.setScene(overGame);
				window.setTitle("Game over");
				stop();
			}
		}
	}


	private int i = 10;
	public void labelActionPerformed(java.awt.event.ActionEvent evt) {
		Timer timer = new Timer();
		AnimationTimer roundNoChecker = new RoundNoChecker();
		ClientMakeChocieChecker(new ActionEvent(client, 0, null));
		timer.scheduleAtFixedRate(new TimerTask() {
			int k=1;
			@Override
			public void run() {
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {

						if(i==0) {
							roundNoChecker.start();
							during.label.setText(Integer.toString(i));
							k++;
							if(k>scheduledExecutionTime()) {
								cancel();
							}
							i=10;
						}
						else {
							during.label.setText(Integer.toString(i--));
						}
					}
				});
			}
			public long scheduledExecutionTime() {
				return client.getModeInt();
			}
		}, 0, 1000);
	}

	private class RoundNoChecker extends AnimationTimer 
	{
		@Override
		public void handle(long arg0)
		{
			if(client.getRoundNoInt().equals(roundNoInt)) 
			{

			}
			else if(client.getRoundNoInt()>client.getModeInt()) {
				stop();
			}
			else if(client.getRoundNoInt().compareTo(roundNoInt) == 1)
			{
				//System.out.println(client.getRoundNoInt());
				during.label4.setText("");
				during.label1.setText("Your choice is "+client.rdp.getResultList().get(roundNoInt - 1).getYourChoice().getChoiseName());
				during.label2.setText("Your Opponent's choice is "+client.rdp.getResultList().get(roundNoInt - 1).getOpponentChoice().getChoiseName());
				roundNoInt = client.getRoundNoInt();
				stop();
			}

			else 
			{
				//System.out.print("INCONSISTENT");
			}
		}
	}


	private class ExceptionallyStopped extends AnimationTimer{
		Stage window;
		public ExceptionallyStopped(Stage window) {
			this.window = window;
		}
		public void handle(long arg0)
		{
			if(exceptionallyStopped == false) {
				checkStop();
			}
			else {


				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning!");
				alert.setHeaderText("Exception Occurs");
				alert.setContentText("Please restart the game.");
				alert.show();
				alert.setOnCloseRequest(e -> {
					if (alert.getResult() == ButtonType.OK) {
						window.close();
						System.exit(0);
					}
					else if (alert.getResult() == ButtonType.CANCEL) 
					{
						window.close();
						System.exit(0);
					}
					else 
					{
						window.close();
						System.exit(0);
					}
				});

				stop();
			}
		}
	}

	
	private class ClientExitChecker extends AnimationTimer{
		Stage window;
		public ClientExitChecker(Stage window) {
			this.window = window;
		}
		public void handle(long arg0)
		{
			if(ClientMain.clientExited == false) {
				ClientMain.checkClientExit();
			}
			else {

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning!");
				alert.setHeaderText("Your opponent has quit.");
				alert.setContentText("Please restart the game.");
				alert.show();
				alert.setOnCloseRequest(e -> {
					if (alert.getResult() == ButtonType.OK) {
						window.close();
						System.exit(0);
					}
					else if (alert.getResult() == ButtonType.CANCEL) 
					{
						window.close();
						System.exit(0);
					}
					else 
					{
						window.close();
						System.exit(0);
					}
				});

				stop();
			}
		}
	}
	

	public void ClientMakeChocieChecker(java.awt.event.ActionEvent evt) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int i=1;
			@Override
			public void run() {
				if(client.getMakeChoice()) {
					client.setMakeChoice(false);
					i++;
				}
				else{
					try {
						client.choose(Choice.GESTURES.ROCK);
						i++;
						client.setMakeChoice(false);

					} catch (ClassNotFoundException e) {

						e.printStackTrace();
					}
				}
				if(i>scheduledExecutionTime()) {
					cancel();
				}

			}
			public long scheduledExecutionTime() {
				return client.getModeInt();
			}
		}, 10000, 11000);
	}

	public void label3ActionPerformed(java.awt.event.ActionEvent evt) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int k=1;
			@Override
			public void run() {
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {
						client.setCanChoose(true);
						during.label1.setText("");
						during.label2.setText("");
						during.label3.setText("Round "+k+" :");
						during.label4.setText("Please make your choice in 10 seconds\n(The default choice:rock)");
						k++;
					}
				});
			}
			public long scheduledExecutionTime() {
				return client.getModeInt();
			}
		}, 0, 11000);
	}

	public static void checkStartGame() 
	{
		hasStarted=client.getHasStarted();
	}

	public static void checkEndGame() 
	{
		hasStopped = client.getHasStopped();
	}

	public static Scene getStartWelcomePage() {
		return startWelcomePage;
	}
	public static void checkStop() 
	{
		exceptionallyStopped = client.isHasExceptionallyStopped();
	}
	public static void checkClientExit() 
	{
		ClientMain.clientExited = client.isClientExited();
	}
}