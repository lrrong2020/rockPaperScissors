package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

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
	private int roomNo;
	private boolean isHost = false;
	private boolean hasStarted = false;
	Semaphore initializationSemaphore = new Semaphore(1);
	private static Semaphore resultSemaphore = new Semaphore(1);

	//construct a thread
	public HandleAClient(Socket socket) 
	{
		/** Initialization **/
		this.socket = socket;

		/** To be implemented
			check if the socket has already been put in the user map **/

		/* Display connection results */
		// Display the time
		ConsoleServer.log("\n============Starting a thread for client at " + new Date() + "============\n");

		// Find the client's host name, and IP address
		InetAddress inetAddress = socket.getInetAddress();
		ConsoleServer.log("Client [" + ConsoleServer.CLIENT_HANDLER_MAP.size() + "] 's host name is " + inetAddress.getHostName() + "\n"
				+ "IP Address is " + inetAddress.getHostAddress() + "\n");

		//display all UUIDs of users who has registered in the user map
		//		ConsoleServer.checkAllUsers();
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

	public int getRoomNo()
	{
		return roomNo;
	}

	public boolean isHost()
	{
		return isHost;
	}

	public void setHost(boolean isHost)
	{
		this.isHost = isHost;
	}

	public boolean isHasStarted()
	{
		return hasStarted;
	}

	public void setHasStarted(boolean hasStarted)
	{
		this.hasStarted = hasStarted;
	}

	public void setRoomNo(int roomNo)
	{
		this.roomNo = roomNo;
	}

	//send initial data to the client
	public void sendInitBean() throws InterruptedException, IOException 
	{	
		ConsoleServer.log("RoomNo: " + getRoomNo());
		Room room = ConsoleServer.getRoom(getRoomNo());

		room.hostSemaphore.acquire();
		boolean isHost = room.getClientHandlers().size() == 1 ? true:false;
		ConsoleServer.log("room.getClientHandlers().size() : " + room.getClientHandlers().size());
		room.hostSemaphore.release();

		this.setHost(isHost);
		DataBean idb = new InitBean(this.getUUID(), isHost);//indicates that if the user is the host (first registered user)

		//send the initial DataBean to the client
		try
		{
			this.outputToClient.writeObject(idb);
			this.outputToClient.flush();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setHasStarted(true);
	}

	public void sendStartBean(int m) throws IOException 
	{
		ConsoleServer.log("Sending start Bean");
		DataBean idb = new StartBean(m);//default constructor to indicates server-sent startBean

		//send the start DataBean to the client
		this.outputToClient.writeObject(idb);
		this.outputToClient.flush();		
	}

	public void sendPreparedBean() throws IOException
	{
		DataBean idb = new PreparedBean(getRoomNo());

		//send the start DataBean to the client
		this.outputToClient.writeObject(idb);
		this.outputToClient.flush();
	}

	public void sendResultBean(Choice c1, Choice c2) throws IOException 
	{
		ConsoleServer.log("Sending result Bean");
		DataBean idb = new ResultBean(c1, c2, ConsoleServer.getRoom(roomNo).getRoundNoInt());//default constructor to indicates server-sent startBean

		//send the start DataBean to the client
		this.outputToClient.writeObject(idb);
		this.outputToClient.flush();	
	}

	public void sendExitBean() throws IOException
	{
		ConsoleServer.log("Sending exception exit bean");
		DataBean idb = new ExitBean();

		//send the start DataBean to the client
		this.outputToClient.writeObject(idb);
		this.outputToClient.flush();	
	}
	
	public void sendExceptionExitBean(Exception exception) throws IOException
	{
		ConsoleServer.log("Sending exception exit bean");
		DataBean idb = new ExceptionExitBean(exception);

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
			//
			//			if(receivedSBean.getPlayer().getIsHost()) 
			//			{
			//				//starts the game
			//				ConsoleServer.startGame(receivedSBean.getMode());
			//			}
			//			else 
			//			{
			//				//do nothing
			//			}

			//send StartBean to all users indicates that the game is on
			//			this.outputToClient.writeObject(new StartBean());//incomplete constructor
			ConsoleServer.log("Starting game (HandleAClient)");
			ConsoleServer.startGame(receivedSBean.getMode(), ConsoleServer.getRoom(getRoomNo()));
		}


		//atomic!!!
		else if(receivedBean instanceof ChoiceBean)
		{

			ConsoleServer.log("Received Bean: " + receivedBean.toString() + "\n");

			if(((ChoiceBean) receivedBean).getRoundNoInt().equals(Integer.valueOf(0))) 
			{
				sendExceptionExitBean(new ChoiceBeforeGameStartException("Not started yet"));
				return;
			}

			try
			{
				resultSemaphore.acquire();

				//put the (ChoiceBean) in class-level Choice list
				ConsoleServer.getRoom(roomNo).getClientChoiceBeans().add(new ChoiceBean[2]);

				//choice bean list contains 2 choices?
				ChoiceBean[] currentRoundChoiceBeans = ConsoleServer.getRoom(roomNo).getClientChoiceBeans().get(ConsoleServer.getRoom(roomNo).getRoundNoInt() - 1);
				if(currentRoundChoiceBeans[0] == null)
				{	
					currentRoundChoiceBeans[0] = (ChoiceBean) receivedBean;
				}
				else 
				{
					currentRoundChoiceBeans[1] = (ChoiceBean) receivedBean;

					if(currentRoundChoiceBeans[0].getRoundNoInt().equals(ConsoleServer.getRoom(roomNo).getRoundNoInt())) 
					{
						//broadcast when the second choice bean is appended

						try 
						{
							ConsoleServer.getRoom(roomNo).sendResults(ConsoleServer.getRoom(roomNo).getRoundNoInt() - 1);
						} 
						catch (ClassNotFoundException e) 
						{
							ConsoleServer.log("[Error]-ClassNotFound");
							e.printStackTrace();
						} 
						catch (IOException e) 
						{
							ConsoleServer.log("[Error]-IO");
							e.printStackTrace();
						} 
						catch (ChoiceMoreThanOnceException e) 
						{
							try 
							{
								ConsoleServer.log("[Error Choice more than once]");
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
							ConsoleServer.log("[Error Synchronization]");
							sendExceptionExitBean(new DataInconsistentException("Inconsistent"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
			finally 
			{
				ConsoleServer.log("releasing lock...");
				resultSemaphore.release();
			}
		}
		else if (receivedBean instanceof ExitBean) 
		{
			for (Entry<UUID, HandleAClient> entry : ConsoleServer.getRoom(getRoomNo()).getClientHandlers().entrySet()) 
			{
				entry.getValue().sendExitBean();
			}
		}		
				
		else 
		{
			ConsoleServer.log("WHat bean?");
		} 
	}

	// Create data input and output streams
	public void initializeIOStreams() throws IOException 
	{
		//note that outputStream should always be defined first!
		this.outputToClient = new ObjectOutputStream(socket.getOutputStream());//put it first
		this.inputFromClient = new ObjectInputStream(socket.getInputStream());
	}



	/** Run a thread for each client **/
	@Override
	public void run() 
	{
		try {
			initializeIOStreams();
			sendInitBean();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Continuously serve the client
		while(!exit) 
		{	
			try
			{	
				handleReceivedBean();
			}
			catch(IOException | ClassNotFoundException ex) 
			{
				//				ex.printStackTrace();//debug
				ConsoleServer.log("============\n============\n");
				ConsoleServer.log("Client UUID:" + this.getUUID() + " quit\n============\n============");

				try
				{
					//					ConsoleServer.log("acquiring");
					ConsoleServer.exitSemaphore.acquire();
					ConsoleServer.clientExit(getRoomNo() ,this.uuid);
					if(ConsoleServer.getRoom(getRoomNo()).getClientHandlers().size() == 1) 
					{
						for (Entry<UUID, HandleAClient> entry : ConsoleServer.getRoom(getRoomNo()).getClientHandlers().entrySet()) 
						{
							entry.getValue().sendExceptionExitBean(new OpponentExitException("opponent exit"));
						}
					}
					ConsoleServer.exitSemaphore.release();
					//					ConsoleServer.log("releasing");
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//				ConsoleServer.checkAllUsers();

				//send ExceptionExitBean to clients
				this.stop();
			}
		}
	}

	//terminate the thread handling a client
	public void stop()
	{
		//		try
		//		{	
		//			this.getSocket().close();
		//		} catch (IOException e)
		//		{
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		setHasStarted(false);
		exit = true;
	}
}
