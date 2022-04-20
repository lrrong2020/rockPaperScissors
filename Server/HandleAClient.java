package rockPaperScissors.Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

import rockPaperScissors.Model.DataBeans.*;
import rockPaperScissors.Model.*;
import rockPaperScissors.Exceptions.*;

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
		Server.log("\n============Starting a thread for client at " + new Date() + "============\n");

		// Find the client's host name, and IP address
		InetAddress inetAddress = socket.getInetAddress();
		Server.log("Client [" + Server.CLIENT_HANDLER_MAP.size() + "] 's host name is " + inetAddress.getHostName() + "\n"
				+ "IP Address is " + inetAddress.getHostAddress() + "\n");

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
		Server.log("RoomNo: " + getRoomNo());
		Room room = Server.getRoom(getRoomNo());

		room.hostSemaphore.acquire();
		boolean isHost = room.getClientHandlers().size() == 1 ? true:false;
		Server.log("room.getClientHandlers().size() : " + room.getClientHandlers().size());
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
			e.printStackTrace();
		}
		this.setHasStarted(true);
	}

	public void sendStartBean(int m) throws IOException 
	{
		Server.log("Sending start Bean");
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

	public void sendResultBean(Choice yourChoice, Choice opponentChoice) throws IOException 
	{
		Server.log("Sending result Bean");
		DataBean idb = new ResultBean(c1, c2, Server.getRoom(roomNo).getRoundNoInt());//default constructor to indicates server-sent startBean

		//send the start DataBean to the client
		this.outputToClient.writeObject(idb);
		this.outputToClient.flush();	
	}

	public void sendExitBean() throws IOException
	{
		Server.log("Sending exception exit bean");
		DataBean idb = new ExitBean();

		//send the start DataBean to the client
		this.outputToClient.writeObject(idb);
		this.outputToClient.flush();	
	}

	public void sendExceptionExitBean(Exception exception) throws IOException
	{
		Server.log("Sending exception exit bean");
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
			
			//send StartBean to all users indicates that the game is on
			//			this.outputToClient.writeObject(new StartBean());//incomplete constructor
			Server.log("Starting game (HandleAClient)");
			Server.startGame(receivedSBean.getMode(), Server.getRoom(getRoomNo()));
		}


		//atomic!!!
		else if(receivedBean instanceof ChoiceBean)
		{
			Server.log("Received Bean: " + receivedBean.toString() + "\n");

			if(((ChoiceBean) receivedBean).getRoundNoInt().equals(Integer.valueOf(0))) 
			{
				sendExceptionExitBean(new ChoiceBeforeGameStartException("Not started yet"));
				return;
			}

			try
			{
				resultSemaphore.acquire();

				//put the (ChoiceBean) in class-level Choice list
				Server.getRoom(roomNo).getClientChoiceBeans().add(new ChoiceBean[2]);

				//choice bean list contains 2 choices?
				ChoiceBean[] currentRoundChoiceBeans = Server.getRoom(roomNo).getClientChoiceBeans().get(Server.getRoom(roomNo).getRoundNoInt() - 1);
				if(currentRoundChoiceBeans[0] == null)
				{	
					currentRoundChoiceBeans[0] = (ChoiceBean) receivedBean;
				}
				else 
				{
					currentRoundChoiceBeans[1] = (ChoiceBean) receivedBean;

					if(currentRoundChoiceBeans[0].getRoundNoInt().equals(Server.getRoom(roomNo).getRoundNoInt())) 
					{
						//broadcast when the second choice bean is appended

						try 
						{
							Server.getRoom(roomNo).sendResults(Server.getRoom(roomNo).getRoundNoInt() - 1);
						} 
						catch (ClassNotFoundException e) 
						{
							Server.log("[Error]-ClassNotFound");
							e.printStackTrace();
						} 
						catch (IOException e) 
						{
							Server.log("[Error]-IO");
							e.printStackTrace();
						} 
						catch (ChoiceMoreThanOnceException e) 
						{
							try 
							{
								Server.log("[Error Choice more than once]");
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
							Server.log("[Error Synchronization]");
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
				Server.log("releasing lock...");
				resultSemaphore.release();
			}
		}
		else if (receivedBean instanceof ExitBean) 
		{
			for (Entry<UUID, HandleAClient> entry : Server.getRoom(getRoomNo()).getClientHandlers().entrySet()) 
			{
				entry.getValue().sendExitBean();
			}
		}		

		else 
		{
			Server.log("WHat bean?");
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
		} catch (IOException | InterruptedException e) 
		{
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
				Server.log("============\n============\n");
				Server.log("Client UUID:" + this.getUUID() + " quit\n============\n============");

				try
				{
					//					Server.log("acquiring");
					Server.exitSemaphore.acquire();
					Server.clientExit(getRoomNo() ,this.uuid);
					if(Server.getRoom(getRoomNo()).getClientHandlers().size() == 1) 
					{
						for (Entry<UUID, HandleAClient> entry : Server.getRoom(getRoomNo()).getClientHandlers().entrySet()) 
						{
							entry.getValue().sendExceptionExitBean(new OpponentExitException("opponent exit"));
						}
					}
					Server.exitSemaphore.release();
					//					Server.log("releasing");
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{

					e.printStackTrace();
				}

				//send ExceptionExitBean to clients
				this.stop();
			}
		}
	}

	//terminate the thread handling a client
	public void stop()
	{
		setHasStarted(false);
		exit = true;
	}
}
