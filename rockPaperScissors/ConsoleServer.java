package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import rockPaperScissors.rockPaperScissors.Exceptions.*;


public class ConsoleServer
{
	protected static final int PORT = 8000;//for socket connection

	//to store users and identify them with randomly generated universally unique identifier (UUID)
	public static final Map<UUID, Socket> ONLINE_USER_MAP = new ConcurrentHashMap<UUID, Socket>();

	//class-level client lists to synchronize and store data
	public static final List<HandleAClient> CLIENT_HANDLER_LIST = new ArrayList<HandleAClient>();//list of 
	public static final List<HandleAClient> CLIENT_HANDLER_LIST_RM = new ArrayList<HandleAClient>();
	
	public static final List<Room> ROOM_LIST = new ArrayList<Room>();

	protected static int roundNo = 1;

	private Thread socketThread = null;

	public static Semaphore semaphore = new Semaphore(1);
	
	private static HandleTheSocket socketHandler = HandleTheSocket.getInstance();
	
	public static Semaphore exitSemaphore = new Semaphore(1);

	//constructor	
	public ConsoleServer()
	{
		super();
		log("Initializing server");

		socketThread = new Thread(socketHandler);
		socketThread.start();
		log("Server initialized");
	}
	
	//Inner Class
	//handle ServerSocket singleton
	static class HandleTheSocket implements Runnable
	{
		private volatile boolean exit;
		ServerSocket serverSocket = null;
		private static HandleTheSocket socketHandler = new HandleTheSocket();

		private HandleTheSocket()
		{			
			super();
			// Create a server socket
			try 
			{
				this.serverSocket = new ServerSocket(ConsoleServer.PORT);//should close socket for performance
				log("MultiThreadServer started at " + new Date() + '\n');
			}
			catch(Exception e) 
			{
				e.printStackTrace();
			}
		}

		public static HandleTheSocket getInstance() 
		{
			return HandleTheSocket.socketHandler;
		}
		
		

		@Override
		public void run()
		{
			//continuously accept the connections
			while (!exit)
			{
				// Listen for a new connection request
				Socket socket;
				Thread clientThread = null;
				try 
				{
					if(ConsoleServer.ONLINE_USER_MAP.size() <= 2) 
					{
						socket = serverSocket.accept();
						// Create a new thread for the connection
						HandleAClient task = new HandleAClient(socket);

						CLIENT_HANDLER_LIST.add(task);//add

						// Start a new thread for each client
						clientThread = new Thread(task);
						clientThread.start();

						//2 players have registered
						if(ConsoleServer.CLIENT_HANDLER_LIST.size() == 2) //check
						{
							//send startBean to all clients
							log("\nHandleAClient: 2 users have registered\n");
							
							//can start game

						}
					}
					else 
					{
						return;
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
	public static void startGame(int m) 
	{
		log("Starting game for all clients");
		Room room = new Room(ConsoleServer.ONLINE_USER_MAP, ConsoleServer.CLIENT_HANDLER_LIST);
		room.setRoomNoInt(ROOM_LIST.size());
		ConsoleServer.ROOM_LIST.add(room);

		for(HandleAClient h : CLIENT_HANDLER_LIST) 
		{
			try 
			{
				h.setRoomNo(room.getRoomNoInt());
				h.sendStartBean(m);
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
		log("Inconsistency exit");
		for(HandleAClient h : CLIENT_HANDLER_LIST) 
		{
			h.sendExceptionExitBean(new DataInconsistentException("Inconsistent"));
		}
	}

	public static HandleAClient getClientHandler(List<HandleAClient> clientHandlers, Socket socket) 
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
	
	public static Room getRoom(Integer roomNoInt) 
	{
		for (Room room: ConsoleServer.ROOM_LIST) 
		{
			if(room.getRoomNoInt().equals(roomNoInt))
			{
				return room;
			}
		}
		return null;
	}

	public static void clientExit(UUID uuid)
	{
//		ROOM_LIST.remove(getClientHandler(CLIENT_HANDLER_LIST, ONLINE_USER_MAP.get(uuid)).getRoomNo());
//		getClientHandler(CLIENT_HANDLER_LIST, ONLINE_USER_MAP.get(uuid)).stop();
		for (HandleAClient clientHandler : CLIENT_HANDLER_LIST) 
		{
			if(clientHandler.getUUID().equals(uuid)) 
			{
//				CLIENT_HANDLER_LIST.remove(CLIENT_HANDLER_LIST.indexOf(clientHandler));
				removeAHandler(clientHandler);
			}
		}
		ONLINE_USER_MAP.remove(uuid);
		checkAllUsers();
	}
	
	public static void removeAHandler(HandleAClient h) 
	{
		for (HandleAClient clientHandler : CLIENT_HANDLER_LIST) 
		{
			for (HandleAClient cd : CLIENT_HANDLER_LIST_RM) 
			{
				if( clientHandler.getUUID().equals(cd.getUUID())) 
				{
					return;
				}
				else 
				{
					CLIENT_HANDLER_LIST.remove(CLIENT_HANDLER_LIST.indexOf(clientHandler));
				}
			}
		}
	}

	public static void endGame() 
	{
		//send end bean
	}

	public static void checkAllUsers() 
	{
		ConsoleServer.log("All users:");
		for (Entry<UUID, Socket> entry : ConsoleServer.ONLINE_USER_MAP.entrySet()) 
		{
			ConsoleServer.log( "<"+entry.getKey() + ">");//display UUIDs
		}
		if(ConsoleServer.ONLINE_USER_MAP.entrySet().size() == 0) {
			ConsoleServer.log("No User");
		}
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
