package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.net.*;

//logical client
public class Client
{
	//socket parameters to build connection
	private static final String HOST = "localhost";//may use IPv4 address
	private static final int PORT = 8000;//port number
	
	// IOStreams
	//Note that outputStream should always be defined first!
	private static ObjectOutputStream toServer;//defined first
	private static ObjectInputStream fromServer;
	private static Player player = new Player();//holds player singleton

	public Client() throws NullPointerException
	{
		super();
	}

	//initialize socket connection with the server
		private void initializeConnection() throws IOException 
		{
			// Create a socket to connect to the server
			@SuppressWarnings("resource")//need to close socket in consideration of performance 
			Socket socket = new Socket(HOST, PORT);

			// Create an output stream to send object to the server
			toServer = new ObjectOutputStream(socket.getOutputStream());

			// Create an input stream to receive object from the server
			fromServer = new ObjectInputStream(socket.getInputStream());
		}
		
		//handle DataBean to be sent
		public void sendDataBean(DataBean sdBean) 
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
		private void handleReceivedObject(Object objFromServer) throws IOException, ClassNotFoundException, NullPointerException
		{
			if(objFromServer == null) 
			{
				throw new NullPointerException("Read null from the server");
			}
			else
			{
				DataBean receivedBean = (DataBean)objFromServer;
				
				//polymorphism style of handling handling different events or status
				if(receivedBean instanceof InitBean) 
				{
					InitBean receivedIBean = (InitBean)receivedBean;//cast to subclass

					//display initial information
					display("Status: " + receivedIBean.getClass());
					display("Your UUID: " + receivedIBean.getUUID().toString());
					display("You are" + (receivedIBean.getIsHost()?" the ":" not the ") + "host.");

					//set UUID and isHost to the Player instance
					player.setUUID(receivedIBean.getUUID());
					player.setIsHost(receivedIBean.getIsHost());
				}
				else if (receivedBean instanceof StartBean) 
				{
					//when the game starts
					this.gameStart();
				}
				else if (receivedBean instanceof GameOnBean) 
				{
					//server sends 10s count down 
				}
				else if (receivedBean instanceof EndBean) 
				{
					
				}
				else
				{
					//when the Bean is non of defined DataBean
					throw new ClassNotFoundException("Undefined Bean");
				}
			}
		}
		
		//send the player instance to the server indicates that the game starts
		public void gameStart() 
		{
			this.sendDataBean(new StartBean(player));
		}
		
		private void display(String string) 
		{
			TestClientFx.appendTextArea(string);
		}
		
		//initialize the client
		public void initialize() throws ClassNotFoundException, NullPointerException
		{
				//handle object read from the server
				try 
				{
					//initialize IOStreams
					this.initializeConnection();

					//read object from the server through ObjectInputStream
					Object objFromServer = fromServer.readObject();
					
					this.handleReceivedObject(objFromServer);
				}
				catch(IOException e)
				{
					//handle
					e.printStackTrace();
					//may reconnect or do something?
				}
				catch(ClassNotFoundException | NullPointerException e) 
				{
					//Invalid DataBean
					//server passed a null
					throw e;
				}
		}
}
