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
import java.util.*;
import java.util.Map.Entry;

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


	private static Player player = Player.getInstance();

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
			e.printStackTrace();
		}
	}

	//handle received data bean
	private static boolean handleReceiveDataBean(DataBean receiveBean) throws IOException 
	{	

		if(receiveBean == null) 
		{
			System.out.println("Empty DataBean");
			return false;
		}
		else 
		{
			if(receiveBean.getStatus().equals(DataBean.STATUS.INIT)) 
			{
				//	TestClientFx.setUuid(initBean.getUUID());
				//	TestClientFx.setUserMap(initBean.getUserMap());
				//	TestClientFx.isHost = initBean.getIsHost();
				InitBean receiveIBean = (InitBean)receiveBean;
				appendTextArea(ta, "Status: " + receiveIBean.getStatus());
				appendTextArea(ta, "Your UUID:" + receiveIBean.getUUID().toString());
				appendTextArea(ta, "You are" + (receiveIBean.getIsHost()?" the ":" not the ") + "host.");

				player.setUUID(receiveIBean.getUUID());
				player.setIsHost(receiveIBean.getIsHost());
			}
			//
			//			if(receiveBean.getStatus() == DataBean.STATUS.GAME_START) 
			//			{
			//
			//			}
			//
			//			if(receiveBean.getStatus() == DataBean.STATUS.GAME_END) 
			//			{
			//
			//			}

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
			return true;
		}

	}

	//similar syntax for rewriting append method of jTextArea of java.swing
	public static void appendTextArea(TextArea textArea, String str) 
	{
		System.out.println(textArea.getText());
		textArea.setText(textArea.getText() + "\n" +str);
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
			TestClientFx.handleReceiveDataBean((DataBean)fromServer.readObject());

			//	DataBean initBean = (DataBean)fromServer.readObject();
			//	TestClientFx.setUuid(initBean.getUUID());
			//	TestClientFx.setUserMap(initBean.getUserMap());

		}
		catch (IOException | ClassNotFoundException ex) 
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
		Button btSend = new Button("Start");
		btSend.setLayoutX(200);
		btSend.setLayoutY(200);
		Group root = new Group(btSend, ta);

		EventHandler<MouseEvent> eventHandlerSend = new EventHandler<MouseEvent>() 
		{ 
			@Override 
			public void handle (MouseEvent e)
			{	
				System.out.println(e);
				TestClientFx.sendDataBean(new StartBean("Game start", DataBean.STATUS.GAME_START, player));
				//	TestClientFx.handleReceiveDataBean((DataBean)fromServer.readObject());
				//	String sendMessage = "This is client";
				//	DataBean sendBean = new DataBean();
				//	sendBean.setMessage(sendMessage);
				//	sendBean.setUUID(TestClientFx.getUUID());
				//	Send the data to the server
				//	sendDataBean(sendBean);
				//	handleReceiveDataBean((DataBean)fromServer.readObject());		
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