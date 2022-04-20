package rockPaperScissors.Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;


public class Server
{
	protected static final int PORT = 8000;//for socket connection
	private static final int MAX_USERS = 10;

	//to store users and identify them with randomly generated universally unique identifier (UUID)


	//class-level client lists to synchronize and store data
	//	public static final List<HandleAClient> CLIENT_HANDLER_LIST = new ArrayList<HandleAClient>();//list of 

	public static final Map<UUID, HandleAClient> CLIENT_HANDLER_MAP = new ConcurrentHashMap<UUID, HandleAClient>();

	public static final List<Room> ROOM_LIST = new ArrayList<Room>();

	protected static int roundNo = 1;

	private Thread socketThread = null;

	public static Semaphore startAfterInitializeSemaphore = new Semaphore(1);

	private static HandleTheSocket socketHandler = HandleTheSocket.getInstance();

	public static Semaphore exitSemaphore = new Semaphore(1);

	//constructor	
	public Server()
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
				this.serverSocket = new ServerSocket(Server.PORT);//should close socket for performance
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

		public void clientRegister() 
		{
			// Listen for a new connection request
			Socket socket;
			Thread clientThread = null;
			Semaphore userMapSemaphore = new Semaphore(1);
			try 
			{
				if(Server.CLIENT_HANDLER_MAP.size() < MAX_USERS) 
				{

					socket = serverSocket.accept();
					// Create a new thread for the connection
					HandleAClient task = new HandleAClient(socket);

					// Start a new thread for each client
					clientThread = new Thread(task);


					//if it's a new client (i.e. has never registered or put in the user map)
					UUID rdUUID = UUID.randomUUID();//generate a random UUID for each client
					task.setUUID(rdUUID);


					//registration

					//						CLIENT_HANDLER_LIST.add(task);//add
					CLIENT_HANDLER_MAP.put(task.getUUID(), task);


					//semaphore.acquire
					userMapSemaphore.acquire();
					if(Server.CLIENT_HANDLER_MAP.size() == 1) 
					{

						Map<UUID, HandleAClient> clientHandlers = new ConcurrentHashMap<UUID, HandleAClient>();


						clientHandlers.putAll(CLIENT_HANDLER_MAP);
						Room room = new Room(clientHandlers);


						room.setRoomNoInt(ROOM_LIST.size());
						task.setRoomNo(ROOM_LIST.size());
						log("Setting roomNo ... " + ROOM_LIST.size());
						Server.ROOM_LIST.add(room);
						room.checkAllUsers();


					}

					//2 players have registered
					else if(Server.CLIENT_HANDLER_MAP.size() == 2) //check
					{
						Map<UUID, HandleAClient> clientHandlers = new ConcurrentHashMap<UUID, HandleAClient>();

						clientHandlers.putAll(CLIENT_HANDLER_MAP);


						//send startBean to all clients
						log("\nHandleAClient: 2 users have registered\n");

						//can start game
						log("ROOM_LIST.size()" + ROOM_LIST.size());
						Room room = Server.getRoom(ROOM_LIST.size() - 1);

						room.setClientHandlers(clientHandlers);
						task.setRoomNo(room.getRoomNoInt());


						for (Entry<UUID, HandleAClient> entry : Server.CLIENT_HANDLER_MAP.entrySet()) 
						{
							log("Clearing " + entry.getKey().toString() + "in user map");
							Server.CLIENT_HANDLER_MAP.remove(entry.getKey());
						}

						room.checkAllUsers();
						room.getHostHandler().sendPreparedBean();
					}
					else 
					{
						//error
						log("[Error] - MAP size bigger than 2");
					}
					userMapSemaphore.release();

					clientThread.start();

				}
				else 
				{
					return;
				}
			} 
			catch (IOException | InterruptedException e) 
			{
				e.printStackTrace();
			}
		}

		@Override
		public void run()
		{
			//continuously accept the connections
			while (!exit)
			{
				clientRegister();
			}		
		}
		public void stop()
		{
			exit = true;
		}
	}
	//end of inner class	

	//class level start game
	public static void startGame(int m, Room room) throws IOException 
	{
		log("Starting game for all clients");


		room.startGame(m);


	}

	public static Room getRoom(Integer roomNoInt) 
	{
		for (Room room: Server.ROOM_LIST) 
		{
			if(room.getRoomNoInt().equals(roomNoInt))
			{
				return room;
			}
		}
		log("Returning null in getRoom");
		return null;
	}

	public static void clientExit(int roomNo, UUID uuid)
	{

		Room room = getRoom(roomNo);
		//check if game is on and send ExitBean

		log("Removing: " + uuid.toString());
		getRoom(roomNo).getClientHandlers().remove(uuid);

		room.checkAllUsers();
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
		new Server();
	}
}
