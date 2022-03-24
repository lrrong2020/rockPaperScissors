package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.net.*;
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

	//socket parameters to build connection
	private static final String HOST = "localhost";//may use IPv4 address
	private static final int PORT = 8000;//port number

	// IO streams
	//Note that outputStream should always be defined first!
	private static ObjectOutputStream toServer;
	private static ObjectInputStream fromServer;
	private static Player player = Player.getInstance();//holds player singleton

	public TestClientFx() 
	{	
		try 
		{
			TestClientFx.initializeClient();
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();//debug
			appendTextArea("\n" + e.toString() + "\n");
		}
	}	

	//handle DataBean to be sent
	private static void sendDataBean(DataBean sdBean) 
	{
		try 
		{
			toServer.writeObject(sdBean);
			toServer.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	//handle received data bean
	private static void handleReceiveObject(Object objFromServer) throws IOException, ClassNotFoundException
	{

		if(objFromServer == null) 
		{
			throw new NullPointerException("Read null from the server");
		}
		else
		{
			DataBean receiveBean = (DataBean)objFromServer;
			
			//polymorphism style of handling handling different events or status
			if(receiveBean instanceof InitBean) 
			{
				InitBean receiveIBean = (InitBean)receiveBean;//cast to subclass

				//display initial information
				appendTextArea("Status: " + receiveIBean.getClass());
				appendTextArea("Your UUID: " + receiveIBean.getUUID().toString());
				appendTextArea("You are" + (receiveIBean.getIsHost()?" the ":" not the ") + "host.");

				//set UUID and isHost to the Player instance
				player.setUUID(receiveIBean.getUUID());
				player.setIsHost(receiveIBean.getIsHost());
			}
			else if (receiveBean instanceof StartBean) 
			{
				//when the game starts
			}
			else if (receiveBean instanceof GameOnBean) 
			{
				//when the 
			}
			else if (receiveBean instanceof EndBean) 
			{
				
			}
			else
			{
				//when the Bean is non of defined DataBean
				throw new ClassNotFoundException("Undefined Bean");
			}
		}
	}

	//similar syntax for rewriting append method of jTextArea of java.swing
	//use it the same way as System.out.println(String string) !
	public static void appendTextArea(String string) 
	{
		ta.setText(ta.getText() + "\n" +string);
		System.out.println("\n" + string);//debug
	}

	private static void initializeConnection() throws UnknownHostException, IOException 
	{
		// Create a socket to connect to the server
		@SuppressWarnings("resource")//need to close socket in consideration of performance 
		Socket socket = new Socket(HOST, PORT);

		// Create an output stream to send object to the server
		toServer = new ObjectOutputStream(socket.getOutputStream());

		// Create an input stream to receive object from the server
		fromServer = new ObjectInputStream(socket.getInputStream());
	}

	private static void initializeClient() throws NullPointerException
	{
		appendTextArea("Initailizing");
		try 
		{
			//initialize IOStreams
			TestClientFx.initializeConnection();

			//read object from the server through ObjectInputStream
			Object objFromServer = fromServer.readObject();

			//handle object read from the server
			handleReceiveObject(objFromServer);
		}
		catch (IOException | ClassNotFoundException ex) 
		{
			ex.printStackTrace();//debug
			appendTextArea("\n" + ex.toString() + "\n");
		}
	}

	public static Group getRoot() //JavaFX Group
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

				//send the player instance to the server indicates that the game starts
				TestClientFx.sendDataBean(new StartBean(player));
			}
		};

		// bind the event listener to the button
		btSend.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSend);

		return root;
	}

	public static void showScene(Stage stage) //JavaFX scene
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

	@Override
	public void start(Stage stage) throws Exception
	{
		appendTextArea("\nDisplaying Client");
		showScene(stage);
	}

	public static void main(String[] args) 
	{
		launch(args);
	}
}