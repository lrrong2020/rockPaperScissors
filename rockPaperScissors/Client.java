package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.net.*;

import rockPaperScissors.rockPaperScissors.DataBeans.*;

//logical client
public class Client
{
	//socket parameters to build connection
	private static String host = "localhost";//may use IPv4 address
	private static int port = 8000;//port number
	private Socket socket;
	// IOStreams
	//Note that outputStream should always be defined first!
	private ObjectOutputStream toServer;//defined first
	private ObjectInputStream fromServer;
	
	private Player player = new Player();//holds player singleton
	private Thread objectListener = null;//class-level thread to continuously listen to the server
	private Integer roundNoInt = Integer.valueOf(0);//round number
	private int mode = 0;
	private boolean isHost = false;
	
	private boolean canChoose = false;
	private Thread countDownThread;
	
	//constructors
	public Client()
	{
		super();
	}

	public Client(String host, int port) 
	{
		this.setHost(host);
		this.setPort(port);
	}

	//setters and getters
	public void setHost(String host) 
	{
		Client.host = host;
	}

	public void setPort(int port) 
	{
		Client.port = port;
	}

	public void setRoundNoInt(Integer roundNoInt)
	{
		this.roundNoInt = roundNoInt;
	}
	public Integer getRoundNoInt()
	{
		return roundNoInt;
	}

	
	public void setMode(int mode)
	{
		this.mode = mode;
	}
	public int getMode()
	{
		return mode;
	}
	
	public void setCanChoose(boolean canChoose)
	{
		this.canChoose = canChoose;
	}
	public boolean getCanChoose()
	{
		return canChoose;
	}

	public boolean isHost()
	{
		return isHost;
	}

	public void setIsHost(boolean isHost)
	{
		this.isHost = isHost;
	}

	//initialize socket connection with the server
	private void initializeConnection() throws IOException 
	{
		// Create a socket to connect to the server

		this.socket = new Socket(host, port);

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
				this.setIsHost(receivedIBean.getIsHost());
			}
			else if (receivedBean instanceof StartBean) 
			{
				//when the game starts
				display("Received Bean is instanceof StartBean");
				gameStart(((StartBean) receivedBean).getMode());
			}
			else if (receivedBean instanceof ResultBean) 
			{
				ResultBean resultBean = (ResultBean)receivedBean;

				this.setRoundNoInt(Integer.valueOf(resultBean.getRoundNoInt().intValue() + 1));//auto boxing?

				display("==========");
				display("[Round]"+resultBean.getRoundNoInt().toString());
				display("Your choice: " + resultBean.getYourChoice().getChoiseName());
				display("Your opponent's choice: " + resultBean.getOpponentChoice().getChoiseName());

				//display results calculated in client
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
				
				//during the game
				
				if(resultBean.getRoundNoInt().intValue() < this.getMode()) 
				{
					//control the choice
					roundStart();
				}
				else 
				{
					display("Game over.");
				}
			}
			else
			{
				//when the Bean is non of defined DataBean
				throw new ClassNotFoundException("Undefined Bean");//more custom exceptions comming!
			}
		}
	}

	public void hostStartGame(int m) 
	{
		display("Host starting game" + "\nBO"+m);
		sendDataBean(new StartBean(m));
	}
	
	//send the player instance to the server indicates that the game starts
	public void gameStart(int m) 
	{	
		this.setMode(m);
		display("The game is on!"+"\nBO"+m);
		this.setRoundNoInt(Integer.valueOf(1));
		//		this.sendDataBean(new StartBean(player));
		roundStart();
	}
	
	//count down timer for round time
	private void roundStart()
	{
		int seconds = 10;
		display("Round["+this.getRoundNoInt().intValue()+"] begins! Please make your choice in " + seconds + " seconds.");
		this.setCanChoose(true);
//		
//		countDownThread = new Thread() {
//			public void run() 
//			{	
//				setCanChoose(true);
//				for(int j = seconds;j > 0;j--) 
//				{
//					//	display count down i s
//					display(j+"");
//					try 
//					{
//						sleep(1000);
//					} 
//					catch (InterruptedException e) 
//					{
//						//do nothing
//					}
//				}
//				setCanChoose(false);
//			}
//		};
//		countDownThread.start();
	}

	//the client made his/her choice
	public void choose(String choiceName) throws ClassNotFoundException 
	{
		if(this.getCanChoose()) 
		{
//			this.countDownThread.interrupt();
			this.setCanChoose(false);
			ChoiceBean choiceBean = new ChoiceBean(choiceName, player, this.getRoundNoInt());
			display("Your choice:" + choiceBean.getChoice().getChoiseName());
			this.sendDataBean(choiceBean);
		}
		else 
		{
			display("You can't choose now!");
		}

	}

	//abstract and encapsulate display function
	private static void display(String string)
	{
		System.out.println(string);
		
		//need to invoke display function of View layer
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
				while(true)
				{
					try 
					{
						//read object from the server through ObjectInputStream
						objFromServer = fromServer.readObject();
						
						//server inform that the client should exit
						if(objFromServer instanceof ExitBean) 
						{	
							if(objFromServer instanceof ExceptionExitBean) 
							{
								((ExitBean) objFromServer).getException().printStackTrace();
								display("Exception Occurs");
								objectListener.interrupt();//terminates the listener
							}
							break;
						}
						display("Successfully get an object!");
						handleReceivedObject(objFromServer);
					}
					catch(ClassNotFoundException e) 
					{
						display("[Error]-ClassNotFound Please restart.");
						e.printStackTrace();
					}
					catch (NullPointerException e) 
					{
						e.printStackTrace();
						display("[Error]-Null Please restart.");
						display(e.toString());
						return;
					}
					catch (IOException e) 
					{
						display("[Warning]-IO Disconnect");
						return;
					}
				}

				//terminates the client
				display("Exit");
			}
		};

		objectListener.start();
	}

	//terminate the client
	public void stop() 
	{
		try
		{
			this.socket.close();
			objectListener.interrupt();
		} catch (IOException e)
		{
			display("[Warning]-IO");
			e.printStackTrace();
		}
		display("The client stoped");
	}
}
