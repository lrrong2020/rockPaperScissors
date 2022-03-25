package rockPaperScissors.rockPaperScissors;

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
		Button btSend = new Button("Start");//create new button instance
		btSend.setLayoutX(200);
		btSend.setLayoutY(200);

		//set root Group & bind button and TextArea to it
		Group root = new Group(btSend, ta);

		//listen the mouse event and handle the event
		EventHandler<MouseEvent> eventHandlerSend = new EventHandler<MouseEvent>() 
		{ 
			@Override 
			public void handle (MouseEvent e)
			{	
				System.out.println(e);//display full event for debug

				//starts the game
				client.gameStart();
			}
		};

		// bind the event listener to the button
		btSend.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSend);

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