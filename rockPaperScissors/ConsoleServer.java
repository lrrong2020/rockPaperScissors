package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ConsoleServer
{
	private static final int PORT = 8000;//for socket connection

	//to store users and identify them with randomly generated universally unique identifier (UUID)
	private static final Map<UUID, Socket> ONLINE_USER_MAP = new ConcurrentHashMap<UUID, Socket>();	
	private static final int MAX_NO_OF_USERS = 2; //assume there are only 2 users

	//constructor	
	public ConsoleServer() 
	{
		super();
		System.out.println("Initializing server");
		try 
		{
			// Create a server socket
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(PORT);//should close socket for performance
			System.out.println("MultiThreadServer started at " + new Date() + '\n');

			//continuously accept the connections
			while (ONLINE_USER_MAP.size() < MAX_NO_OF_USERS)
			{
				// Listen for a new connection request
				Socket socket = serverSocket.accept();

				// Create a new thread for the connection
				HandleAClient task = new HandleAClient(socket);

				// Start a new thread for each client
				new Thread(task).start();
			}
		}
		catch(IOException ex) 
		{
			System.err.println(ex);
		}
	}

	/** Inner Class **/
	// Define the thread class for handling new connection
	class HandleAClient implements Runnable 
	{
		private Socket socket; // A connected socket

		//connections using IOStreams
		private ObjectOutputStream outputToClient = null;
		private ObjectInputStream inputFromClient = null;

		private UUID uuid = null;//uniquely identify the users

		//construct a thread
		public HandleAClient(Socket socket) 
		{
			/** Initialization **/
			this.socket = socket;

			/** To be implemented
			check if the socket has already been put in the user map **/

			//if it's a new client (i.e. has never registered or put in the user map)
			UUID rdUuid = UUID.randomUUID();//generate a random UUID for each client
			this.setUUID(rdUuid);

			//registration
			ConsoleServer.ONLINE_USER_MAP.put(rdUuid , socket);//register a user

			/* Display connection results */
			// Display the time
			System.out.println("Starting a thread for client at " + new Date() + '\n');

			// Find the client's host name, and IP address
			InetAddress inetAddress = socket.getInetAddress();
			System.out.println("Client [" + ConsoleServer.ONLINE_USER_MAP.size() + "] 's host name is " + inetAddress.getHostName() + "\n"
					+ "IP Address is " + inetAddress.getHostAddress() + "\n");

			//display all UUIDs of users who has registered in the user map
			System.out.println("All users:");
			for (Entry<UUID, Socket> entry : ConsoleServer.ONLINE_USER_MAP.entrySet()) 
			{
				System.out.println( " <"+entry.getKey() + "> ");//display UUIDs
			}
		}

		//setter and getters
		public void setUUID(UUID uuid)
		{
			this.uuid = uuid;
		}
		public UUID getUUID()
		{
			return this.uuid;
		}

		//send initial data to the client
		public void sendInit() throws IOException 
		{
			DataBean idb = new InitBean(this.getUUID(),
					ConsoleServer.ONLINE_USER_MAP.size() == 1 ? true:false);//indicates that if the user is the host (first registered user)

			//send the initial DataBean to the client
			this.outputToClient.writeObject(idb);
			this.outputToClient.flush();		
		}

		//handle received DataBean from client
		public void handleReceiveBean() throws IOException, ClassNotFoundException
		{
			//handle Data sent by the client
			DataBean receiveBean = (DataBean)this.inputFromClient.readObject();//read object from input stream

			if(receiveBean instanceof StartBean) //polymorphism style handling different events or status
			{	
				StartBean receiveSBean = (StartBean)receiveBean;//cast to StartBean

				/** only host can start the game
				StartGame operation should not open to non-host player
				which is to be implemented in the front end or View part **/
				
				if(receiveSBean.getPlayer().getIsHost()) 
				{
					//starts the game
				}
				else 
				{
					//do nothing
				}

				//send MatchBean to all users indicates that the game is on
				this.outputToClient.writeObject(new GameOnBean());//incomplete constructor
			}
		}

		// Create data input and output streams
		public void initializeIOStreams() throws IOException 
		{
			//note that outputStream should always be defined first!
			this.outputToClient = new ObjectOutputStream(socket.getOutputStream());
			this.inputFromClient = new ObjectInputStream(socket.getInputStream());
		}


		/** Run a thread for each client **/
		@Override
		public void run() 
		{
			// Continuously serve the client
			while(true) 
			{	
				try
				{
					initializeIOStreams();
					sendInit();
					handleReceiveBean();
				}
				catch(IOException | ClassNotFoundException ex) 
				{
					ex.printStackTrace();
				}
			}
		}
	}
	//end of inner class

	public static void main(String args[]) 
	{
		new ConsoleServer();
	}
}
