package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

import rockPaperScissors.rockPaperScissors.DataBeans.*;
import rockPaperScissors.rockPaperScissors.Exceptions.*;

//Define the thread class for handling new connection
class HandleAClient implements Runnable 
{
	private volatile boolean exit;
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
		DataBean idb = new ResultBean(c1, c2, Integer.valueOf(ConsoleServer.roundNo));//default constructor to indicates server-sent startBean

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


		//atomic!!!
		else if(receivedBean instanceof ChoiceBean) 
		{
			System.out.println("Received Bean: " + receivedBean.toString() + "\n");

			if(((ChoiceBean) receivedBean).getRoundNoInt().equals(Integer.valueOf(0))) 
			{
				sendExceptionExitBean(new ChoiceBeforeGameStartException("Not started yet"));
				return;
			}

			//put the (ChoiceBean) in class-level Choice list
			ConsoleServer.CLIENT_CHOICE_BEAN_LIST.add(new ChoiceBean[2]);

			//choice bean list contains 2 choices?
			ChoiceBean[] currentRoundChoiceBeans = ConsoleServer.CLIENT_CHOICE_BEAN_LIST.get(ConsoleServer.roundNo - 1);
			if(currentRoundChoiceBeans[0] == null)
			{	
				currentRoundChoiceBeans[0] = (ChoiceBean) receivedBean;
			}
			else 
			{
				currentRoundChoiceBeans[1] = (ChoiceBean) receivedBean;

				if(currentRoundChoiceBeans[0].getRoundNoInt().equals(Integer.valueOf(ConsoleServer.roundNo))) 
				{
					//broadcast when the second choice bean is appended

					try 
					{
						ConsoleServer.sendResults(ConsoleServer.roundNo - 1);
					} 
					catch (ClassNotFoundException e) 
					{
						System.out.println("[Error]-ClassNotFound");
						e.printStackTrace();
					} 
					catch (IOException e) 
					{
						System.out.println("[Error]-IO");
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
		else 
		{
			System.out.println("WHat bean?");
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
		while(!exit) 
		{	
			try
			{	
				if(!socket.isClosed()) 
				{
					handleReceivedBean();
				}
			}
			catch(IOException | ClassNotFoundException ex) 
			{
				ex.printStackTrace();//debug
				System.out.println("============\n============\n");
				System.out.println("Client UUID:" + this.getUUID() + " quit\n============\n============");
				//					//remove a client from user map
				//					ConsoleServer.ONLINE_USER_MAP.remove(this.getUUID());

				this.printAllUsers();

				//send ExceptionExitBean to clients
				this.stop();
			}
		}
		System.out.println();
	}

	//terminate the thread handling a client
	public void stop()
	{
		try
		{	
			this.getSocket().close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		exit = true;
	}
}