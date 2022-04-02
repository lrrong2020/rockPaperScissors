package rockPaperScissors.rockPaperScissors;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

public class TestClientFx extends Application
{
	// Text area to display contents
	private static TextArea ta = new TextArea();
	private static Client client = null;
	private Scene welcomePage;
	private static ArrayList<EventHandler>listeners=new ArrayList<>();
	

	//constructors
	public TestClientFx() 
	{	
		super();	
		//create a new client class
		TestClientFx.client = new Client();
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
		Pane root=new Pane();
		welcomePage=new Scene(root,600,400);
		welcomePage.getStylesheets().add(getClass().getResource("PagesSettings.css").toExternalForm());
		
		Image icon=new Image("/rockPaperScissors/rockPaperScissors/media/icon.gif");
		ImageView icon1=new ImageView(icon);
		icon1.setFitHeight(150);
		icon1.setFitWidth(200);
		
		Label label1=new Label("Rock Paper Scissors Game");
		label1.getStyleClass().add("labelContent");

		label1.layoutXProperty().bind(root.widthProperty().divide(3.2));
		Button bt1=new Button("One round");
		Button bt2=new Button("Best two out of three");
		Button bt3=new Button("Best three out of five");
	
		bt1.layoutXProperty().bind(root.widthProperty().divide(1.5));
		bt2.layoutXProperty().bind(bt1.layoutXProperty());
		bt3.layoutXProperty().bind(bt1.layoutXProperty());
		
		bt1.layoutYProperty().bind(root.heightProperty().divide(3));
		bt2.layoutYProperty().bind(root.heightProperty().divide(2));
		bt3.layoutYProperty().bind(root.heightProperty().divide(1.5));
		
		
		icon1.layoutXProperty().bind(root.widthProperty().divide(12));
		icon1.layoutYProperty().bind(root.heightProperty().divide(1.8));
		
		bt1.setOnAction(e->{
			DuringTheGame during=new DuringTheGame();
			Scene duringGame=new Scene(during.CreateGamePage(),600,400);
			duringGame.getStylesheets().add(getClass().getResource("GamePageSettings.css").toExternalForm());
			getEvent();
			int i=0;
			for(Node node:during.CreateGamePage().getChildrenUnmodifiable()) {
				node.addEventHandler(MouseEvent.MOUSE_CLICKED,listeners.get(i));
				i++;
			}
			Stage window=(Stage)bt1.getScene().getWindow();
			window.setTitle("Game started");
			window.setScene(duringGame);
			
		});
		bt2.setOnAction(e->{
			DuringTheGame during=new DuringTheGame();
			Scene duringGame=new Scene(during.CreateGamePage(),600,400);
			duringGame.getStylesheets().add(getClass().getResource("GamePageSettings.css").toExternalForm());
			Stage window=(Stage)bt2.getScene().getWindow();
			window.setTitle("Game started");
			window.setScene(duringGame);
		});
		bt3.setOnAction(e->{
			DuringTheGame during=new DuringTheGame();
			Scene duringGame=new Scene(during.CreateGamePage(),600,400);
			duringGame.getStylesheets().add(getClass().getResource("GamePageSettings.css").toExternalForm());
			Stage window=(Stage)bt3.getScene().getWindow();
			window.setTitle("Game started");
			window.setScene(duringGame);
		});
		
		root.getChildren().addAll(icon1,label1,bt1,bt2,bt3);
		
	}

	//similar syntax for rewriting append method of jTextArea of java.swing
	//use it the same way as System.out.println(String string) !
	public static void appendTextArea(String string) 
	{
		ta.setText(ta.getText() + "\n" +string);
		System.out.println("\n" + string);//debug
	}

	//get JavaFX Group


	public static void getEvent()
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


		// bind the event listener to the button
		//rock.addEventHandler(MouseEvent.MOUSE_CLICKED, rockListener);
		//paper.addEventHandler(MouseEvent.MOUSE_CLICKED, paperListener);
		//scissors.addEventHandler(MouseEvent.MOUSE_CLICKED, scissorsListener);

		
	}
	

	//start JavaFX application
	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setTitle("Welcome to the Rock Paper Scissors Game!");
    	CreateWelcomePage();
    	stage.setScene(welcomePage);
	    stage.show();
	}

	//main method to launch JavaFX application
	public static void main(String[] args) 
	{
		launch(args);
	}
}
