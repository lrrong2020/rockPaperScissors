package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.*;
import javax.swing.*;

public class MultiThreadServer extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int PORT = 8000;
	// Text area for displaying contents
	private JTextArea jta = new JTextArea();
	private static final Map<java.util.UUID, Socket> ONLINE_USER_MAP = new ConcurrentHashMap<java.util.UUID, Socket>();

	public static void main(String args[]) 
	{
		System.out.println("Launching Server");
		new MultiThreadServer();
	}

	public MultiThreadServer() 
	{
		System.out.println("Initializing server");
		this.display();
		try 
		{
			// Create a server socket
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(PORT);
			jta.append("MultiThreadServer started at " + new Date() + '\n');

			// Number a client
			int clientNo = 1;

			while (true) 
			{
				// Listen for a new connection request
				Socket socket = serverSocket.accept();

				// Display the client number
				jta.append("Starting thread for client " + clientNo + " at " + new Date() + '\n');

				// Find the client's host name, and IP address
				InetAddress inetAddress = socket.getInetAddress();
				jta.append("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
				jta.append("Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + "\n");

				// Create a new thread for the connection
				HandleAClient task = new HandleAClient(socket);

				// Start the new thread
				new Thread(task).start();

				// Increment clientNo
				clientNo++;
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
		/** Construct a thread */
		public HandleAClient(Socket socket) 
		{
			this.socket = socket;
			UUID rdUuid = UUID.randomUUID();//initialize a random UUID for each client
			this.setUuid(rdUuid);
			MultiThreadServer.ONLINE_USER_MAP.put(rdUuid , socket);//store users in userMap
		}

		/** Run a thread */
		public void run() 
		{
			try 
			{
				// Create data input and output streams
				//note that outputStream should always be defined first
				ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());

				// Continuously serve the client
				while (true) 
				{
					//initial DataBean
					outputToClient.writeObject(new DataBean("INI_SERVER", this.getUUID()));

					String sendMessage = "This is server";
					DataBean receiveBean = (DataBean) inputFromClient.readObject();
					jta.append("\n" + receiveBean.getCreatedDate() + " " + socket.getInetAddress() + ": " + receiveBean.getMessage());
					DataBean sendBean = new DataBean();
					sendBean.setMessage(sendMessage);
					outputToClient.writeObject(sendBean);
				}
			}
			catch(IOException | ClassNotFoundException e) 
			{
				System.err.println(e);
			} 
		}

		public void setUuid(UUID uuid)
		{
			this.uuid = uuid;
		}
		public UUID getUUID()
		{
			return this.uuid;
		}
	}

	private void display() 
	{	
		System.out.println("Displaying server");
		// Place text area on the frame
		setLayout(new BorderLayout());
		add(new JScrollPane(jta), BorderLayout.CENTER);
		setTitle("MultiThreadServer");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true); // It is necessary to show the frame here!
	}
}