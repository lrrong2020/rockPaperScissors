/*WARNING!!!

You need to launch client individually instead of using the main

*/


package rockPaperScissors.rockPaperScissors;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group; 
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;  

public class Main extends Application
{ 
	@Override
	public void start(Stage stage) 
	{ 
		Button btStartServer = new Button("Start Server");
		Button btStartClient = new Button("Start Client");
		
		btStartServer.setLayoutX(100);
		btStartServer.setLayoutY(80);
		//Creating a Group 
		Group root = new Group(btStartServer, btStartClient); 
		//event handler
		EventHandler<MouseEvent> eventHandlerStartServer = new EventHandler<MouseEvent>() 
		{ 
			@Override 
			public void handle(MouseEvent e) {
				Thread serverThread = new Thread () 
				{
					public void run () 
					{
						new MultiThreadServer();
					}
				};
				serverThread.start();

			} 
		};
		
		EventHandler<MouseEvent> eventHandlerStartClient = new EventHandler<MouseEvent>() 
		{ 
			@Override 
			public void handle(MouseEvent e) {
				Thread clientThread = new Thread () 
				{
					public void run () 
					{
						try {
							new TestClientFx().start(stage);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						};
					}
				};
				clientThread.start();
			} 
		};
		
		btStartServer.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerStartServer);
		btStartClient.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerStartClient);
		
		//Creating a Scene 
		Scene scene = new Scene(root, 600, 300); 

		//Setting title to the scene 
		stage.setTitle("Sample application"); 

		//Adding the scene to the stage 
		stage.setScene(scene); 

		//Displaying the contents of a scene 
		stage.show(); 
	}      
	public static void main(String args[])
	{ 
		launch(args); 
	} 
}       
