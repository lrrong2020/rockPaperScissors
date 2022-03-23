package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ConsoleServer
{
	private static final int PORT = 8000;
	private static final Map<UUID, Socket> ONLINE_USER_MAP = new ConcurrentHashMap<UUID, Socket>();

	public static void main(String args[]) 
	{
		System.out.println("Launching Server");
		new ConsoleServer();
	}

	public ConsoleServer() 
	{
		System.out.println("Initializing server");
		try 
		{
			// Create a server socket
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("MultiThreadServer started at " + new Date() + '\n');

			while (true) 
			{
				// Listen for a new connection request
				Socket socket = serverSocket.accept();

				// Create a new thread for the connection
				HandleAClient task = new HandleAClient(socket);

				// Start the new thread
				new Thread(task).start();
			}
		}
		catch(IOException ex) 
		{
			System.err.println(ex);
		}
	}

	// Inner class
	// Define the thread class for handling new connection
	class HandleAClient implements Runnable 
	{
		private Socket socket; // A connected socket
		private UUID uuid = null;
		private ObjectOutputStream outputToClient = null;
		private ObjectInputStream inputFromClient = null;
		/** Construct a thread */
		public HandleAClient(Socket socket) 
		{
			this.socket = socket;
			UUID rdUuid = UUID.randomUUID();//initialize a random UUID for each client
			this.setUuid(rdUuid);
			ConsoleServer.ONLINE_USER_MAP.put(rdUuid , socket);//store users in userMap

			// Display the client number
			System.out.println("Starting thread for client " + ConsoleServer.ONLINE_USER_MAP.size() + " at " + new Date() + '\n');

			// Find the client's host name, and IP address
			InetAddress inetAddress = socket.getInetAddress();
			System.out.println("Client [" + ConsoleServer.ONLINE_USER_MAP.size() + "] 's host name is " + inetAddress.getHostName() + "\n");
			System.out.println("Client [" + ConsoleServer.ONLINE_USER_MAP.size() + "] 's IP Address is " + inetAddress.getHostAddress() + "\n");

			for (Entry<UUID, Socket> entry : ConsoleServer.ONLINE_USER_MAP.entrySet()) 
			{
				System.out.println( " <"+entry.getKey() + "> ");
			}
		}

		/** Run a thread */
		public void run() 
		{
			try 
			{
				// Create data input and output streams
				//note that outputStream should always be defined first
				this.setOutputToClient(new ObjectOutputStream(socket.getOutputStream()));
				this.setInputFromClient(new ObjectInputStream(socket.getInputStream()));

				// Continuously serve the client
				while(true) 
				{
					DataBean idb = new DataBean //complete 
							(
									"First message", 
									this.getUUID(),
									"INIT",
									ConsoleServer.ONLINE_USER_MAP.size() == 1 ? true:false
									);
					this.getOutputToClient().writeObject(idb);
				}
				//receive data from client
				//					DataBean receiveBean = (DataBean) this.getInputFromClient().readObject();
				//					System.out.println("\n" + receiveBean.getCreatedDate() + " " + socket.getInetAddress() + ": " + receiveBean.getMessage());
				//					
				//reply the client
				//					String sendMessage = "This is server";
				//					DataBean sendBean = new DataBean();
				//					sendBean.setMessage(sendMessage);
				//					this.getOutputToClient().writeObject(sendBean);

			}
			catch(Exception e) 
			{
				e.printStackTrace();
			} 
		}

		public boolean sendInit() 
		{
			return true;
		}

		public void setUuid(UUID uuid)
		{
			this.uuid = uuid;
		}
		public UUID getUUID()
		{
			return this.uuid;
		}

		public ObjectOutputStream getOutputToClient()
		{
			return outputToClient;
		}

		public void setOutputToClient(ObjectOutputStream outputToClient)
		{
			this.outputToClient = outputToClient;
		}

		public ObjectInputStream getInputFromClient()
		{
			return inputFromClient;
		}

		public void setInputFromClient(ObjectInputStream inputFromClient)
		{
			this.inputFromClient = inputFromClient;
		}
	}
}