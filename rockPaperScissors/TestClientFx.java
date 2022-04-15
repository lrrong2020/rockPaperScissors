package rockPaperScissors.rockPaperScissors;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TestClientFx extends Application
{
	// Text area to display contents
	private static TextArea ta = new TextArea();

	public static Client client = null;
	public static boolean hasStarted = false;
	public static boolean hasStopped = false;

	//private Scene findIPPage;
	private Scene welcomePage;
	public static DuringTheGame during=new DuringTheGame();
	public static Parent nodes=during.CreateGamePage();
	private WelcomePage start=new WelcomePage();
	private static ArrayList<EventHandler<MouseEvent>>listeners=null;
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

		Scene startWelcomePage=new Scene(start.getWelcomePage(),600,400);
		startWelcomePage.getStylesheets().add(getClass().getResource("PagesSettings.css").toExternalForm());


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

				start.bt1.setOnAction(m->{
					
					Stage window1=(Stage)start.bt1.getScene().getWindow();
					window1.setTitle("Game started");
					try {
						client.hostStartGame(1);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					setDuringGameScene(window1);
					
					
				});
				start.bt2.setOnAction(m->{
					
					Stage window1=(Stage)start.bt2.getScene().getWindow();
					window1.setTitle("Game started");
					try {
						client.hostStartGame(3);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					setDuringGameScene(window1);
					
					
				});
				start.bt3.setOnAction(m->{
					
					Stage window1=(Stage)start.bt3.getScene().getWindow();
					window1.setTitle("Game started");
					try {
						client.hostStartGame(5);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					setDuringGameScene(window1);
					
					
				});
					
					//window.setScene(duringGame);
				//});

				AnimationTimer am1=new StartEndChecker(window);
				am1.start();
			}
			else {

				WaitingPage waiting=new WaitingPage();
				Scene waitingRes=new Scene(waiting.CreateWaitingPage(),600,400);
				waitingRes.getStylesheets().add(getClass().getResource("PagesSettings.css").toExternalForm());
				window=(Stage)enter.getScene().getWindow();
				window.setScene(waitingRes);
				window.setTitle("Game will be started in several seconds");
				


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
				AnimationTimer am = new StartGameChecker(window);
				am.start();
				//						client.s.release();

				//						System.out.println("TestClientFx released. The available is"+client.s.availablePermits());
				//						}
				AnimationTimer am1=new StartEndChecker(window);
				am1.start();

			}	
			client.initSemaphore.release();
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
					client.stop();
				Platform.exit();
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
		Scene duringGame=new Scene(root,600,400);
		duringGame.getStylesheets().add(getClass().getResource("GamePageSettings.css").toExternalForm());
		window.setScene(duringGame);
		window.setTitle("Game started");
		AnimationTimer amend = new StartEndChecker(window);
		amend.start();
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
	private int i = 10;
	public void labelActionPerformed(java.awt.event.ActionEvent evt) {
	    Timer timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {
	    	int k=1;
	        @Override
	        public void run() {
	        	javafx.application.Platform.runLater(new Runnable() {
	                @Override
	                public void run() {
	                		if(i==0) {
	                			during.label.setText(Integer.toString(i));
	                			k++;
	                			if(k>scheduledExecutionTime()) {
		                			cancel();
		                		}
	                			try {
									TimeUnit.SECONDS.sleep(3);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
	                			i=12;
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
	    }, 3000, 1000);
	    }


	public static void checkStartGame() 
	{
		hasStarted=client.getHasStarted();
	}

	public static void checkEndGame() 
	{
		hasStopped = client.getHasStopped();
	}

}