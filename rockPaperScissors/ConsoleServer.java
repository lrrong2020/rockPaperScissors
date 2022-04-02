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
		private volatile boolean exit;
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
			while (!exit && ConsoleServer.ONLINE_USER_MAP.size() <= ConsoleServer.MAX_NO_OF_USERS)
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
		public void stop()
		{
			exit = true;
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

	public static void clientExit(UUID uuid)
	{
		getClientHandler(ONLINE_USER_MAP.get(uuid)).stop();
		ONLINE_USER_MAP.remove(uuid);
	}
	
	public static void endGame() 
	{
		//send end bean
	}

	public static void log(String string) 
	{
		System.out.println(string);
	}
	
	public static void main(String args[]) 
	{
		new ConsoleServer();
	}
}
