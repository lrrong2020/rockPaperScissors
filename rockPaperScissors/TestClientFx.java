package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.net.*;
import java.util.UUID;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.util.*;

public class TestClientFx extends Application
{
	// Text area to display contents
	private static TextArea ta = new TextArea();

	// IO streams
	//note that outputStream should always be defined first
	private static ObjectOutputStream toServer;
	private static ObjectInputStream fromServer;
	private static final String HOST = "localhost";
	private static final int PORT = 8000;
	private static UUID uuid = null;
	private static Map<UUID, Socket> userMap = null;
	private static boolean hasInit = false;
	private static boolean isHost = false;

	public TestClientFx() 
	{	
		TestClientFx.initializeClient();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//handle received data bean
	private static boolean handleReceiveDataBean(DataBean receiveBean) 
	{	
		if(receiveBean == null) 
		{
			
		}
		
		if(receiveBean.getStatus() == DataBean.STATUS.INIT) 
		{
			DataBean initBean;
			try
			{
				initBean = (DataBean)fromServer.readObject();
				TestClientFx.setUuid(initBean.getUUID());
				TestClientFx.setUserMap(initBean.getUserMap());
				TestClientFx.isHost = initBean.getIsHost();
				TestClientFx.hasInit = TestClientFx.handleReceiveDataBean(initBean);
			} 
			catch (ClassNotFoundException | IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(receiveBean.getStatus() == DataBean.STATUS.GAME_START) 
		{
			
		}
		
		if(receiveBean.getStatus() == DataBean.STATUS.GAME_END) 
		{
			
		}
	
//		if(!TestClientFx.hasInit)
//		{	
//			//handling initial connection
//			appendTextArea(ta, "\n" + receiveBean.getCreatedDate() + " Server: " + receiveBean.getMessage());
//			appendTextArea(ta, "\nYour UUID: " + receiveBean.getUUID().toString());
//		}
//		else 
//		{
//			appendTextArea(ta, "\n" + receiveBean.getCreatedDate() + " Server: " + receiveBean.getMessage());
//		}			
		return receiveBean != null;
	}

	//similar syntax for rewriting append method of jTextArea of java.swing
	public static void appendTextArea(TextArea textArea, String str) 
	{
		System.out.println(textArea.getText());
		textArea.setText(textArea.getText() + str);
	}
	public static UUID getUUID()
	{
		return uuid;
	}
	public static void setUuid(UUID uuid)
	{
		TestClientFx.uuid = uuid;
	}
	public static Map<UUID, Socket> getUserMap()
	{
		return TestClientFx.userMap;
	}
	public static void setUserMap(Map<UUID, Socket> userMp)
	{
		TestClientFx.userMap = userMp;
	}
	private static void initializeClient() 
	{
		appendTextArea(ta, "Initailizing");
		try 
		{
			// Create a socket to connect to the server
			@SuppressWarnings("resource")
			Socket socket = new Socket(HOST, PORT);
			// Socket socket = new Socket("130.254.204.36", 8000);
			// Socket socket = new Socket("drake.Armstrong.edu", 8000);

			// Create an input stream to receive object from the server
			fromServer = new ObjectInputStream(socket.getInputStream());

			// Create an output stream to send object to the server
			toServer = new ObjectOutputStream(socket.getOutputStream());

//			while(!TestClientFx.hasInit) 
//			{	
//				DataBean initBean = (DataBean)fromServer.readObject();
//				TestClientFx.setUuid(initBean.getUUID());
//				TestClientFx.setUserMap(initBean.getUserMap());
//				TestClientFx.hasInit = TestClientFx.handleReceiveDataBean(initBean);
//			}
		}
		catch (IOException ex) 
		{
			appendTextArea(ta, ex.toString() + "\n");
		}
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		appendTextArea(ta, "\nDisplaying Client");
		
		//Creating a Scene 
		Scene scene = new Scene(getRoot(), 600, 300); 

		//Setting title to the scene 
		stage.setTitle("Client"); 

		//Adding the scene to the stage 
		stage.setScene(scene); 

		//Displaying the contents of a scene 
		stage.show(); 
	}
	
	public static Group getRoot() 
	{
		Button btSend = new Button("Test");
		btSend.setLayoutX(200);
		btSend.setLayoutY(200);
		Group root = new Group(btSend, ta);

		EventHandler<MouseEvent> eventHandlerSend = new EventHandler<MouseEvent>() 
		{ 
			@Override 
			public void handle (MouseEvent e)
			{
				// TODO Auto-generated method stub
				try 
				{
					String sendMessage = "This is client";
					DataBean sendBean = new DataBean();
					sendBean.setMessage(sendMessage);
					sendBean.setUUID(TestClientFx.getUUID());
					// Send the data to the server
					sendDataBean(sendBean);
					handleReceiveDataBean((DataBean)fromServer.readObject());				
				}
				catch (IOException | ClassNotFoundException e1) 
				{
					e1.printStackTrace();
				}
			}
		};
		btSend.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerSend);
		return root;
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}