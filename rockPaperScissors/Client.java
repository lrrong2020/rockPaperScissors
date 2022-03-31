package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.net.*;

import rockPaperScissors.rockPaperScissors.DataBeans.*;

//logical client
public class Client
{
	//socket parameters to build connection
	private static final String HOST = "localhost";//may use IPv4 address
	private static final int PORT = 8000;//port number

	// IOStreams
	//Note that outputStream should always be defined first!
	private ObjectOutputStream toServer;//defined first
	private ObjectInputStream fromServer;
	private Player player = new Player();//holds player singleton
	private Thread objectListener = null;//class-level thread to continuously listen to the server
	private Integer roundNoInt = Integer.valueOf(1);//round number

	public Client()
	{
		super();
	}

	//initialize socket connection with the server
	private void initializeConnection() throws IOException 
	{
		// Create a socket to connect to the server
		@SuppressWarnings("resource")//need to close socket in consideration of performance 
		Socket socket = new Socket(HOST, PORT);

		// Create an output stream to send object to the server
		toServer = new ObjectOutputStream(socket.getOutputStream());

		// Create an input stream to receive object from the server
		fromServer = new ObjectInputStream(socket.getInputStream());
	}

	//handle DataBean to be sent
	public void sendDataBean(DataBean sdBean) 
	{
		try 
		{
			toServer.writeObject(sdBean);
			toServer.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}


	//handle received data bean
	private void handleReceivedObject(Object objFromServer) throws IOException, ClassNotFoundException, NullPointerException
	{
		if(objFromServer == null) 
		{
			throw new NullPointerException("Read null from the server");
		}
		else
		{
			DataBean receivedBean = (DataBean)objFromServer;

			//polymorphism style of handling handling different events or status
			if(receivedBean instanceof InitBean) 
			{
				InitBean receivedIBean = (InitBean)receivedBean;//cast to subclass

				//display initial information
				display("Status: " + receivedIBean.getClass());
				display("Your UUID: " + receivedIBean.getUUID().toString());
				display("You are" + (receivedIBean.getIsHost()?" the ":" not the ") + "host.");

				//set UUID and isHost to the Player instance
				player.setUUID(receivedIBean.getUUID());
				player.setIsHost(receivedIBean.getIsHost());
			}
			else if (receivedBean instanceof StartBean) 
			{
				//when the game starts
				display("Received Bean is instanceof StartBean");
				gameStart();
			}
			else if (receivedBean instanceof ResultBean) 
			{
				ResultBean resultBean = (ResultBean)receivedBean;
				
				this.setRoundNoInt(Integer.valueOf(resultBean.getRoundNoInt().intValue() + 1));//auto boxing?
				
				display("==========");
				display("[Round]"+resultBean.getRoundNoInt().toString());
				display("Your choice: " + resultBean.getYourChoice().getChoiseName());
				display("Your opponent's choice: " + resultBean.getOpponentChoice().getChoiseName());
				switch(resultBean.getYourChoice().wins(resultBean.getOpponentChoice())) 
				{
				case 0:
					display("You Lose");
					break;
				case 1:
					display("Tie");
					break;
				case 2:
					display("You Win!");
					break;
				}
				display("==========");
			}
			else
			{
				//when the Bean is non of defined DataBean
				throw new ClassNotFoundException("Undefined Bean");
			}
		}
	}

	//send the player instance to the server indicates that the game starts
	public void gameStart() 
	{
		display("The game is on!");
		this.setRoundNoInt(Integer.valueOf(1));
		//		this.sendDataBean(new StartBean(player));

		//		countDown(10);
	}
	private static void countDown(int i) 
	{
		Thread thread = new Thread() {
			public void run() 
			{
				for(int j = i;j > 0;j--) 
				{
					display(j+"");
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}

	public void choose(String choiceName) throws ClassNotFoundException 
	{
		ChoiceBean choiceBean = new ChoiceBean(choiceName, player, this.getRoundNoInt());
		display("Your choice:" + choiceBean.getChoice().getChoiseName());
		this.sendDataBean(choiceBean);
	}


	private static void display(String string) //abstract and encapsulate
	{
		FooFrontEndFx.appendTextArea(string);
	}

	//initialize the client
	public void initialize() throws ClassNotFoundException, NullPointerException, IOException
	{
		//handle object read from the server
		//initialize IOStreams
		this.initializeConnection();

		//start a new thread to continuously listen to the server
		
		//need to be closed after client terminated
		objectListener = new Thread() {
			public void run() 
			{
				Object objFromServer = null;
				do
				{
					try 
					{
						//read object from the server through ObjectInputStream
						objFromServer = fromServer.readObject();
						display("Successfully get an object!");
						handleReceivedObject(objFromServer);
					} 
					catch (ClassNotFoundException | NullPointerException | IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						display("Error. Please restart.");
						display(e.toString());
						return;
					}
				}while(!(objFromServer instanceof EndBean || objFromServer instanceof ExitBean || objFromServer instanceof ExceptionExitBean));
			    
				//terminates the client
				display("EXITTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
			}
		};

		objectListener.start();
	}

	public Integer getRoundNoInt()
	{
		return roundNoInt;
	}

	public void setRoundNoInt(Integer roundNoInt)
	{
		this.roundNoInt = roundNoInt;
	}
}
