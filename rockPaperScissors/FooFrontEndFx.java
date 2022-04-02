package rockPaperScissors.rockPaperScissors;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.layout.*;

public class FooFrontEndFx extends Application
{
	// Text area to display contents
	private static TextArea ta = new TextArea();
	private static Client client = null;

	//constructors
	public FooFrontEndFx() 
	{	
		super();	
		//create a new client class
		FooFrontEndFx.client = new Client();
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

		Button quit = new Button("Quit");


		rock.setLayoutX(200);
		rock.setLayoutY(200);

		paper.setLayoutX(260);
		paper.setLayoutY(200);

		scissors.setLayoutX(320);
		scissors.setLayoutY(200);

		quit.setLayoutX(380);
		quit.setLayoutX(200);

		Pane root = new Pane();
		root.getChildren().addAll(rock,paper,scissors,ta,quit);


		rock.layoutXProperty().bind(root.widthProperty().divide(3));
		rock.layoutYProperty().bind(root.heightProperty().multiply(0.87));
		paper.layoutXProperty().bind(root.widthProperty().divide(2));
		paper.layoutYProperty().bind(root.heightProperty().multiply(0.87));
		scissors.layoutXProperty().bind(root.widthProperty().divide(1.5));
		scissors.layoutYProperty().bind(root.heightProperty().multiply(0.87));
		ta.layoutXProperty().bind(root.widthProperty().divide(14));
		ta.layoutYProperty().bind(root.heightProperty().multiply(0.1));

		//listen the mouse event and handle the event
		EventHandler<MouseEvent> quitListener = new EventHandler<MouseEvent>() 
		{ 
			@Override 
			public void handle (MouseEvent e)
			{	
				client.stop();
			}
		};
		quit.addEventHandler(MouseEvent.MOUSE_CLICKED, quitListener);


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

	//display JavaFX scene
	public static void showScene(Stage stage) 
	{
		//Creating a Scene 
		Scene scene = new Scene(getRoot(), 600, 300); 

		//Setting title to the scene 
		stage.setTitle("Client"); 

		//Adding the scene to the stage 
		stage.setScene(scene); 

		//Displaying the contents of a scene 
		stage.show(); 
	}

	//start JavaFX application
	@Override
	public void start(Stage stage) throws Exception
	{
		appendTextArea("\nDisplaying Client");
		showScene(stage);
	}

	//main method to launch JavaFX application
	public static void main(String[] args) 
	{
		launch(args);
	}
}