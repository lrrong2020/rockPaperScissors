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

public class TestClientFx extends Application
{
	// Text area to display contents
	private static TextArea ta = new TextArea();
	private static Client client = null;

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

	//similar syntax for rewriting append method of jTextArea of java.swing
	//use it the same way as System.out.println(String string) !
	public static void appendTextArea(String string) 
	{
		ta.setText(ta.getText() + "\n" +string);
		System.out.println("\n" + string);//debug
	}

	//get JavaFX Group
	public static Group getRoot()
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

		//set root Group & bind button and TextArea to it
		Group root = new Group(rock,paper,scissors, ta);

		//listen the mouse event and handle the event
		EventHandler<MouseEvent> rockListener = new EventHandler<MouseEvent>() 
		{ 
			@Override 
			public void handle (MouseEvent e)
			{	
				try 
				{
					client.gameOn(Choice.GESTURES.ROCK);
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
					client.gameOn(Choice.GESTURES.PAPER);
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
					client.gameOn(Choice.GESTURES.SCISSORS);
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