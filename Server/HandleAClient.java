package Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

import Model.DataBeans.*;
import Model.*;
import Exceptions.*;

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
	private static Semaphore resultSemaphore = new Semaphore(1);

	//construct a thread
	public HandleAClient(Socket socket) 
	{
		/** Initialization **/
		this.socket = socket;
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

		Room room = Server.getRoom(getRoomNo());

		room.hostSemaphore.acquire();
		boolean isHost = room.getClientHandlers().size() == 1 ? true:false;

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

		DataBean idb = new ResultBean(yourChoice, opponentChoice, Server.getRoom(roomNo).getRoundNoInt());//default constructor to indicates server-sent startBean

		//send the start DataBean to the client
		this.outputToClient.writeObject(idb);
		this.outputToClient.flush();	
	}

	public void sendExitBean() throws IOException
	{

		DataBean idb = new ExitBean();

		//send the start DataBean to the client
		this.outputToClient.writeObject(idb);
		this.outputToClient.flush();	
	}

	public void sendExceptionExitBean(Exception exception) throws IOException
	{

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

			Server.startGame(receivedSBean.getMode(), Server.getRoom(getRoomNo()));
		}


		//atomic!!!
		else if(receivedBean instanceof ChoiceBean)
		{

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
								sendExceptionExitBean(e);
							} catch (Exception ex) 
							{
								
							}
							
						}
					}
					else 
					{
						try {
	
							sendExceptionExitBean(new DataInconsistentException("Inconsistent"));
						} catch (Exception e) 
						{
						}
					}
				}
			}
			catch (InterruptedException e1)
			{
			}
			finally 
			{

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
				try
				{
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
				} catch (InterruptedException e)
				{
				} catch (IOException e)
				{
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
