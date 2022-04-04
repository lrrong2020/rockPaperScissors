package rockPaperScissors.rockPaperScissors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	private static Client client = null;
	//private Scene findIPPage;
	private Scene welcomePage;
	private static ArrayList<EventHandler<MouseEvent>>listeners=new ArrayList<>();
	public TestClientFx() throws InterruptedException {
		TestClientFx.client=new Client();
		appendTextArea("Client generated");
		try 
		{
			client.initialize();
			appendTextArea("Client initialized");
			System.out.println("releasing");

		}
		catch(IOException ioe) 
		{
			appendTextArea("Client initialize failed");

		}
		catch (ClassNotFoundException | NullPointerException e) 
		{
			//Invalid DataBean
			//server passed a null
			e.printStackTrace();
			appendTextArea("Invalid Data from server!");
		}
	}

	//Create the Welcome Page to show
	public void CreateWelcomePage() {
		if(client.isHost()) {
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
			WelcomePage startHost=new WelcomePage();
			Scene startWelcomePage=new Scene(startHost.getWelcomePage(),600,400);
			startWelcomePage.getStylesheets().add(getClass().getResource("PagesSettings.css").toExternalForm());
			enter.setOnAction(e->{
				Stage window=(Stage)enter.getScene().getWindow();
				window.setTitle("Welcome to the Rock Paper Scissors Game!");
				window.setScene(startWelcomePage);
			});
		}
		else {
			WelcomePage start=new WelcomePage();
			welcomePage=new Scene(start.getWelcomePage(),600,400);
			welcomePage.getStylesheets().add(getClass().getResource("PagesSettings.css").toExternalForm());
			
		}
		/*DuringTheGame during=new DuringTheGame();
		Scene duringGame=new Scene(during.CreateGamePage(),600,400);
		duringGame.getStylesheets().add(getClass().getResource("GamePageSettings.css").toExternalForm());
		bt1.setOnAction(e->{
			Stage window=(Stage)bt1.getScene().getWindow();
			window.setTitle("Game started");
			//during.CreateGamePage().getChildrenUnmodifiable()
			window.setScene(duringGame);
			
		});
		bt2.setOnAction(e->{
			Stage window=(Stage)bt2.getScene().getWindow();
			window.setTitle("Game started");
			window.setScene(duringGame);
		});
		bt3.setOnAction(e->{
			Stage window=(Stage)bt3.getScene().getWindow();
			window.setTitle("Game started");
			window.setScene(duringGame);
		});
		
		root.getChildren().addAll(icon1,label1,bt1,bt2,bt3);*/
		
	}

	//similar syntax for rewriting append method of jTextArea of java.swing
	//use it the same way as System.out.println(String string) !
	public static void appendTextArea(String string) 
	{
		ta.setText(ta.getText() + "\n" +string);
		System.out.println("\n" + string);//debug
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

		// bind the event listener to the button
		//rock.addEventHandler(MouseEvent.MOUSE_CLICKED, rockListener);
		//paper.addEventHandler(MouseEvent.MOUSE_CLICKED, paperListener);
		//scissors.addEventHandler(MouseEvent.MOUSE_CLICKED, scissorsListener);

	
	}
	

	//start JavaFX application
	@Override
	public void start(Stage stage) throws Exception
	{	
		//System.out.println("Does it all ????"+client.getHasInitialized());
		//System.out.println("cient is a host??"+client.isHost());
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
}
