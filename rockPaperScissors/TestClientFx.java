package rockPaperScissors.rockPaperScissors;

import java.io.IOException;
import java.util.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TestClientFx extends Application
{
	// Text area to display contents
	private static TextArea ta = new TextArea();

	public static Client client = null;
	public static boolean hasStarted = false;
	public static boolean hasStopped = false;
	public static boolean exceptionallyStopped = false;
	public static AnimationTimer am;
	public static AnimationTimer am1;
	public static AnimationTimer am2;

	//private Scene findIPPage;
	private Scene welcomePage;
	public static Scene startWelcomePage;

	private static ArrayList<EventHandler<MouseEvent>>listeners=new ArrayList<>();
	
	public TestClientFx() {


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

		enter.setOnAction(e->{
			System.out.println(IP.getText().toString().trim());
			String ipAddr=IP.textProperty().get().trim();
			TestClientFx.client=new Client(ipAddr);
			appendTextArea("Client generated");
			try 
			{
				client.initialize();
				appendTextArea("Client initialized");


			}
			catch(IOException ioe) 
			{
				appendTextArea("Client initialize failed");

			}
			catch (ClassNotFoundException | NullPointerException e1) 
			{
				//Invalid DataBean
				//server passed a null
				e1.printStackTrace();
				appendTextArea("Invalid Data from server!");
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			WelcomePage start=new WelcomePage();
			Scene startWelcomePage=new Scene(start.getWelcomePage(),600,400);
			startWelcomePage.getStylesheets().add(getClass().getResource("PagesSettings.css").toExternalForm());

			try {
				client.initSemaphore.acquire();

			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Stage window;
			if(client.getIsHost()) {
				window=(Stage)enter.getScene().getWindow();
				window.setTitle("Welcome to the Rock Paper Scissors Game!");
				window.setScene(startWelcomePage);
				TestClientFx.am1=new StartEndChecker(window);
				am1.start();
				client.getCountDown().start();
				TestClientFx.am2 = new ExceptionallyStopped(window);
				am2.start();
			}
			else {

				WaitingPage waiting=new WaitingPage();
				Scene waitingRes=new Scene(waiting.CreateWaitingPage(),600,400);
				waitingRes.getStylesheets().add(getClass().getResource("PagesSettings.css").toExternalForm());
				window=(Stage)enter.getScene().getWindow();
				window.setScene(waitingRes);
				window.setTitle("Game will be started in several seconds");
				TestClientFx.am2 = new ExceptionallyStopped(window);
				am2.start();
				
				//					try
				//					{
				//						client.s.acquire();
				//					} catch (InterruptedException e2)
				//					{
				//						// TODO Auto-generated catch block
				//						e2.printStackTrace();
				//					}

				//					boolean b = false;
				//					try {
				//						System.out.println("TestClientFx acquiring ...");
				//						client.s.acquire();
				//						b=client.getHasStarted();


				//					} catch (InterruptedException e1) {
				//						// TODO Auto-generated catch block
				//						e1.printStackTrace();
				//					}
				//					if(!hasStarted) {
				TestClientFx.am = new StartGameChecker(window);
				am.start();
				
				client.getCountDown().start();
				//						client.s.release();

				//						System.out.println("TestClientFx released. The available is"+client.s.availablePermits());
				//						}
				TestClientFx.am1=new StartEndChecker(window);
				am1.start();

			}	
			client.initSemaphore.release();
		});
		
		IP.setOnKeyPressed(new EventHandler<KeyEvent>() {
		    

			@Override
		    public void handle(KeyEvent ke) {
		        if (ke.getCode().equals(KeyCode.ENTER)) {
		        	System.out.println(IP.getText().toString().trim());
					String ipAddr=IP.textProperty().get().trim();
					TestClientFx.client=new Client(ipAddr);
					appendTextArea("Client generated");
					try 
					{
						client.initialize();
						appendTextArea("Client initialized");


					}
					catch(IOException ioe) 
					{
						appendTextArea("Client initialize failed");

					}
					catch (ClassNotFoundException | NullPointerException e1) 
					{
						//Invalid DataBean
						//server passed a null
						e1.printStackTrace();
						appendTextArea("Invalid Data from server!");
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					WelcomePage start=new WelcomePage();
					Scene startWelcomePage=new Scene(start.getWelcomePage(),600,400);
					startWelcomePage.getStylesheets().add(getClass().getResource("PagesSettings.css").toExternalForm());

					try {
						client.initSemaphore.acquire();

					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Stage window;
					if(client.getIsHost()) {
						window=(Stage)enter.getScene().getWindow();
						window.setTitle("Welcome to the Rock Paper Scissors Game!");
						window.setScene(startWelcomePage);
						TestClientFx.am1=new StartEndChecker(window);
						am1.start();
						client.getCountDown().start();
						TestClientFx.am2 = new ExceptionallyStopped(window);
						am2.start();
					}
					else {

						WaitingPage waiting=new WaitingPage();
						Scene waitingRes=new Scene(waiting.CreateWaitingPage(),600,400);
						waitingRes.getStylesheets().add(getClass().getResource("PagesSettings.css").toExternalForm());
						window=(Stage)enter.getScene().getWindow();
						window.setScene(waitingRes);
						window.setTitle("Game will be started in several seconds");
						TestClientFx.am2 = new ExceptionallyStopped(window);
						am2.start();

						//					try
						//					{
						//						client.s.acquire();
						//					} catch (InterruptedException e2)
						//					{
						//						// TODO Auto-generated catch block
						//						e2.printStackTrace();
						//					}

						//					boolean b = false;
						//					try {
						//						System.out.println("TestClientFx acquiring ...");
						//						client.s.acquire();
						//						b=client.getHasStarted();


						//					} catch (InterruptedException e1) {
						//						// TODO Auto-generated catch block
						//						e1.printStackTrace();
						//					}
						//					if(!hasStarted) {
						TestClientFx.am = new StartGameChecker(window);
						am.start();
						client.getCountDown().start();
						

						//						client.s.release();

						//						System.out.println("TestClientFx released. The available is"+client.s.availablePermits());
						//						}
						TestClientFx.am1=new StartEndChecker(window);
						am1.start();

					}
				client.initSemaphore.release();
		        }
		    }
		});


	}

	//similar syntax for rewriting append method of jTextArea of java.swing
	//use it the same way as System.out.println(String string) !
	public static void appendTextArea(String string) 
	{
		ta.setText(ta.getText() + "\n" +string);
		System.out.println("\n" + string);//debug
	}

	public static void log(String string) 
	{
		System.out.println(string);
	}


	//get JavaFX Group


	public static ArrayList<EventHandler<MouseEvent>> getEvent()
	{


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
		DuringTheGame during=new DuringTheGame();
		Scene duringGame=new Scene(during.CreateGamePage(),600,400);
		duringGame.getStylesheets().add(getClass().getResource("GamePageSettings.css").toExternalForm());
		window.setScene(duringGame);
		window.setTitle("Game started");
		//AnimationTimer amend = new StartEndChecker(window);
		//amend.start();
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
				overGame.getStylesheets().add(getClass().getResource("GamePageSettings.css").toExternalForm());
				window.setScene(overGame);
				window.setTitle("Game over");
				stop();
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
						alert.setContentText("Click OK to exit the game.");
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


	
}