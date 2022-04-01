package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import rockPaperScissors.rockPaperScissors.DataBeans.*;
import rockPaperScissors.rockPaperScissors.Exceptions.*;


public class ConsoleServer
{
	protected static final int PORT = 8000;//for socket connection

	//to store users and identify them with randomly generated universally unique identifier (UUID)
	protected static final Map<UUID, Socket> ONLINE_USER_MAP = new ConcurrentHashMap<UUID, Socket>();
	protected static final int MAX_NO_OF_USERS = 2; //assume there are only 2 users

	//class-level client lists to synchronize and store data
	protected static final List<HandleAClient> CLIENT_HANDLER_LIST = new ArrayList<HandleAClient>();//list of 
	protected static List<ChoiceBean[]> CLIENT_CHOICE_BEAN_LIST = new ArrayList<ChoiceBean[]>();//results of each round
	protected static int roundNo = 1;

	private Thread socketThread = null;
	//constructor	
	public ConsoleServer() 
	{
		super();
		System.out.println("Initializing server");
		try 
		{
			HandleTheSocket socketHandler = new HandleTheSocket();
			socketThread = new Thread(socketHandler);
			socketThread.start();
		}
		catch(IOException ex) 
		{
			System.err.println(ex);
		}
	}

	//Inner Class
	//handle ServerSocket
	class HandleTheSocket implements Runnable
	{
		ServerSocket serverSocket = null;

		public HandleTheSocket() throws IOException 
		{			
			super();
			// Create a server socket
			this.serverSocket = new ServerSocket(ConsoleServer.PORT);//should close socket for performance
			System.out.println("MultiThreadServer started at " + new Date() + '\n');
		}

		@Override
		public void run()
		{
			//continuously accept the connections
			while (ConsoleServer.ONLINE_USER_MAP.size() <= ConsoleServer.MAX_NO_OF_USERS)
			{
				// Listen for a new connection request
				Socket socket;
				Thread clientThread = null;
				try 
				{
					socket = serverSocket.accept();
					// Create a new thread for the connection
					HandleAClient task = new HandleAClient(socket);
					CLIENT_HANDLER_LIST.add(task);

					// Start a new thread for each client
					clientThread = new Thread(task);
					clientThread.start();

					//2 players have registered
					if(ConsoleServer.CLIENT_HANDLER_LIST.size() == 2) 
					{
						//send startBean to all clients
						System.out.println("\nHandleAClient: 2 users have registered\n");
						ConsoleServer.startGame();
					}
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}		
		}
	}
	//end of inner class

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
			UUID rdUUID = UUID.randomUUID();//generate a random UUID for each client
			this.setUUID(rdUUID);

			//registration
			ConsoleServer.ONLINE_USER_MAP.put(rdUUID , socket);//register a user

			/* Display connection results */
			// Display the time
			System.out.println("\n============Starting a thread for client at " + new Date() + "============\n");

			// Find the client's host name, and IP address
			InetAddress inetAddress = socket.getInetAddress();
			System.out.println("Client [" + ConsoleServer.ONLINE_USER_MAP.size() + "] 's host name is " + inetAddress.getHostName() + "\n"
					+ "IP Address is " + inetAddress.getHostAddress() + "\n");

			//display all UUIDs of users who has registered in the user map
			this.printAllUsers();
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
		public Socket getSocket() {
			return this.socket;
		}

		//send initial data to the client
		public void sendInitBean() throws IOException 
		{
			DataBean idb = new InitBean(this.getUUID(),
					ConsoleServer.ONLINE_USER_MAP.size() == 1 ? true:false);//indicates that if the user is the host (first registered user)

			//send the initial DataBean to the client
			this.outputToClient.writeObject(idb);
			this.outputToClient.flush();		
		}

		public void sendStartBean() throws IOException 
		{
			System.out.println("Sending start Bean");
			DataBean idb = new StartBean();//default constructor to indicates server-sent startBean

			//send the start DataBean to the client
			this.outputToClient.writeObject(idb);
			this.outputToClient.flush();		
		}

		public void sendResultBean(Choice c1, Choice c2) throws IOException 
		{
			System.out.println("Sending result Bean");
			DataBean idb = new ResultBean(c1, c2, Integer.valueOf(roundNo));//default constructor to indicates server-sent startBean

			//send the start DataBean to the client
			this.outputToClient.writeObject(idb);
			this.outputToClient.flush();	
		}

		public void sendExceptionExitBean(Exception exception) throws IOException
		{
			System.out.println("Sending exception exit bean");
			DataBean idb = new ExceptionExitBean(exception);//default constructor to indicates server-sent startBean

			//send the start DataBean to the client
			this.outputToClient.writeObject(idb);
			this.outputToClient.flush();	
		}

		//handle received DataBean from client
		public void handleReceivedBean() throws IOException, ClassNotFoundException
		{
			//handle Data sent by the client
			DataBean receivedBean = (DataBean)this.inputFromClient.readObject();//read object from input stream

			if(receivedBean instanceof StartBean) //polymorphism style handling different events or status
			{	
				StartBean receivedSBean = (StartBean)receivedBean;//cast to StartBean

				/** only host can start the game
				StartGame operation should not open to non-host player
				which is to be implemented in the front end or View part **/

				if(receivedSBean.getPlayer().getIsHost()) 
				{
					//starts the game
				}
				else 
				{
					//do nothing
				}

				//send StartBean to all users indicates that the game is on
				this.outputToClient.writeObject(new StartBean());//incomplete constructor
			}

			if(receivedBean instanceof ChoiceBean) 
			{
				System.out.println("Received Bean: " + receivedBean.toString() + "\n");
				//put the (ChoiceBean) in class-level Choice list
				CLIENT_CHOICE_BEAN_LIST.add(new ChoiceBean[2]);


				if(CLIENT_CHOICE_BEAN_LIST.get(roundNo - 1)[0] == null)
				{	
					ChoiceBean[] choiceBeanArr = CLIENT_CHOICE_BEAN_LIST.get(roundNo - 1);
					choiceBeanArr[0] = (ChoiceBean) receivedBean;
				}
				else 
				{
					ChoiceBean[] choiceBeanArr = CLIENT_CHOICE_BEAN_LIST.get(roundNo - 1);
					choiceBeanArr[1] = (ChoiceBean) receivedBean;

					if(choiceBeanArr[0].getRoundNoInt().equals(Integer.valueOf(roundNo))) 
					{
						//broadcast

						try 
						{
							sendResults(roundNo - 1);
						} 
						catch (ClassNotFoundException e) 
						{

							e.printStackTrace();
						} 
						catch (IOException e) 
						{

							e.printStackTrace();
						} 
						catch (ChoiceMoreThanOnceException e) 
						{
							try 
							{
								System.out.println("[Error Choice more than once]");
								sendExceptionExitBean(e);
							} catch (Exception ex) 
							{
								ex.printStackTrace();
							}
							e.printStackTrace();
						}
					}
					else 
					{
						try {
							System.out.println("[Error Synchronization]");
							sendExceptionExitBean(new DataInconsistentException("Inconsistent"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		// Create data input and output streams
		public void initializeIOStreams() throws IOException 
		{
			//note that outputStream should always be defined first!
			this.outputToClient = new ObjectOutputStream(socket.getOutputStream());//put it first
			this.inputFromClient = new ObjectInputStream(socket.getInputStream());
		}

		private void printAllUsers() 
		{
			System.out.println("All users:");
			for (Entry<UUID, Socket> entry : ConsoleServer.ONLINE_USER_MAP.entrySet()) 
			{
				System.out.println( "<"+entry.getKey() + ">");//display UUIDs
			}
			if(ConsoleServer.ONLINE_USER_MAP.entrySet().size() == 0) {
				System.out.println("No User");
			}
		}

		/** Run a thread for each client **/
		@Override
		public void run() 
		{
			try {
				initializeIOStreams();
				sendInitBean();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Continuously serve the client
			while(true) 
			{	
				try
				{
					handleReceivedBean();
				}
				catch(IOException | ClassNotFoundException ex) 
				{
					ex.printStackTrace();//debug
					System.out.println("============\n============\nWARNING!");
					System.out.println("A client quit\n============\n============");
					System.out.println("Quit client UUID:" + this.getUUID());

					//					//remove a client from user map
					//					ConsoleServer.ONLINE_USER_MAP.remove(this.getUUID());
					this.printAllUsers();

					//send ExceptionExitBean to clients
					return;
				}
			}
		}
	}
	//end of inner class

	//class level start game
	public static void startGame() 
	{
		System.out.println("Starting game for all clients");
		for(HandleAClient h : CLIENT_HANDLER_LIST) 
		{
			try 
			{
				h.sendStartBean();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	//exception occurs  
	public static void sendExceptionExitBean() throws IOException
	{
		System.out.println("Inconsistency exit");
		for(HandleAClient h : CLIENT_HANDLER_LIST) 
		{
			h.sendExceptionExitBean(new DataInconsistentException("Inconsistent"));
		}
	}


	public static void sendResults(int rNoI0) throws ClassNotFoundException, IOException, ChoiceMoreThanOnceException 
	{
		ChoiceBean[] choiceBeanArr = ConsoleServer.CLIENT_CHOICE_BEAN_LIST.get(rNoI0);//0

		ChoiceBean player0ChoiceBean = choiceBeanArr[0];
		ChoiceBean player1ChoiceBean = choiceBeanArr[1];

		if(player0ChoiceBean.equals(player1ChoiceBean)) 
		{
			throw new ChoiceMoreThanOnceException("Write 2 times");
		}

		Socket player0Socket = ONLINE_USER_MAP.get(player0ChoiceBean.getPlayer().getUUID());

		HandleAClient player0Handler = getClientHandler(player0Socket);
		HandleAClient player1Handler = null;

		//find the HandleAClient instance that matches

		//need to control access
		if(player0Socket.equals(CLIENT_HANDLER_LIST.get(0).getSocket())) 
		{
			player0Handler = CLIENT_HANDLER_LIST.get(0);
			player1Handler = CLIENT_HANDLER_LIST.get(1);//get another
		}
		else if(player0Socket.equals(CLIENT_HANDLER_LIST.get(1).getSocket())) 
		{
			player0Handler = CLIENT_HANDLER_LIST.get(1);
			player1Handler = CLIENT_HANDLER_LIST.get(0);//get another
		}
		else throw new ClassNotFoundException("Socket not found");


		System.out.print(player0ChoiceBean.getChoice().toString());
		System.out.print(player1ChoiceBean.getChoice().toString());

		player0Handler.sendResultBean(player0ChoiceBean.getChoice(), player1ChoiceBean.getChoice());
		player1Handler.sendResultBean(player1ChoiceBean.getChoice(), player0ChoiceBean.getChoice());

		roundNo += 1;
	}

	public static HandleAClient getClientHandler(Socket socket) 
	{
		for (HandleAClient clientHandler: ConsoleServer.CLIENT_HANDLER_LIST) 
		{
			if(clientHandler.getSocket().equals(socket))
			{
				return clientHandler;
			}
		}
		return null;
	}

	public static void endGame() 
	{
		//send end bean
	}

	public static void main(String args[]) 
	{
		new ConsoleServer();
	}
}
