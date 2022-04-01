package rockPaperScissors.rockPaperScissors;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
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


	public static Pane getRoot()
	{
		//create button and set its layout to display
		Button rock = new Button("Rock");//create new button instance
		Button paper= new Button("Paper");//create new button instance
		Button scissors = new Button("Scissors");//create new button instance
		rock.setLayoutX(200);
		rock.setLayoutY(200);

		paper.setLayoutX(260);
		paper.setLayoutY(200);

		scissors.setLayoutX(320);
		scissors.setLayoutY(200);

		Pane root = new Pane();
		root.getChildren().addAll(rock,paper,scissors,ta);
		
		
		rock.layoutXProperty().bind(root.widthProperty().divide(3));
		rock.layoutYProperty().bind(root.heightProperty().multiply(0.87));
		paper.layoutXProperty().bind(root.widthProperty().divide(2));
		paper.layoutYProperty().bind(root.heightProperty().multiply(0.87));
		scissors.layoutXProperty().bind(root.widthProperty().divide(1.5));
		scissors.layoutYProperty().bind(root.heightProperty().multiply(0.87));
		ta.layoutXProperty().bind(root.widthProperty().divide(14));
		ta.layoutYProperty().bind(root.heightProperty().multiply(0.1));


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


		// bind the event listener to the button
		rock.addEventHandler(MouseEvent.MOUSE_CLICKED, rockListener);
		paper.addEventHandler(MouseEvent.MOUSE_CLICKED, paperListener);
		scissors.addEventHandler(MouseEvent.MOUSE_CLICKED, scissorsListener);

		return root;
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
