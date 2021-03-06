package Client;

import java.io.*;
import java.net.*;
import java.util.concurrent.Semaphore;
import Model.*;
import Model.DataBeans.*;

//logical client
public class Client 
{
	//socket parameters to build connection
	private String host = "localhost";//may use IPv4 address
	private int port = 8000;//port number
	private Socket socket;
	// IOStreams
	//Note that outputStream should always be defined first!
	private ObjectOutputStream toServer;//defined first!
	private ObjectInputStream fromServer;

	private Player player = new Player();//hide player to view

	private Integer roundNoInt = Integer.valueOf(0);//round number
	private Integer modeInt = Integer.valueOf(0);

	private boolean isHost = false;//the same as player.getIsHost()
	private boolean canStart = false;
	private boolean canChoose = false;

	private boolean hasStarted=false;
	//	private Thread countDownThread;

	//boolean indicate states
	private boolean hasInitialized = false;//determine whether the client has initialized or not
	private boolean hasStopped = false;
	private boolean hasExceptionallyStopped = false;
	private boolean clientExited = false;

	//multiple threads related
	public Semaphore initSemaphore = new Semaphore(1); //can be invoked outside to make sure initialization is done before the client is used
	private Thread objectListener = null;//class-level thread to continuously listen to the server
	private Thread countDownThread = null;//handle the count down timer

	//encapsulated results
	public ResultDisplayBean rdp = new ResultDisplayBean();
	private boolean makeChoice=false;
	private boolean exit = false;

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

	public Client(String host) 
	{
		this.setHost(host);
	}

	//setters and getters
	public void setHost(String host) 
	{
		this.host = host;
	}

	public void setPort(int port) 
	{
		this.port = port;
	}

	public void setRoundNoInt(Integer roundNoInt)
	{
		this.roundNoInt = roundNoInt;
	}
	public Integer getRoundNoInt()
	{
		return roundNoInt;
	}

	//BOX
	public void setModeInt(Integer modeInt)
	{
		this.modeInt = modeInt;
	}
	public Integer getModeInt()
	{
		return modeInt;
	}

	public void setCanChoose(boolean canChoose)
	{
		this.canChoose = canChoose;
	}
	public boolean getCanChoose()
	{
		return canChoose;
	}

	public void setIsHost(boolean isHost)
	{
		this.isHost = isHost;
	}
	public boolean getIsHost()
	{
		return isHost;
	}
	public void setMakeChoice(boolean b) {
		this.makeChoice=b;
	}

	public boolean getMakeChoice() {
		return makeChoice;
	}
	public void setHasStopped(boolean hasStopped)
	{
		this.hasStopped = hasStopped;
	}
	public boolean getHasStopped()
	{
		return hasStopped;
	}

	public void setHasStarted(boolean hasStarted) {
		this.hasStarted = hasStarted;
	}
	public boolean getHasStarted() {
		return this.hasStarted;
	}

	public boolean isHasExceptionallyStopped()
	{
		return hasExceptionallyStopped;
	}

	public void setHasExceptionallyStopped(boolean hasExceptionallyStopped)
	{
		this.hasExceptionallyStopped = hasExceptionallyStopped;
	}

	public boolean isCanStart()
	{
		return canStart;
	}

	public void setCanStart(boolean canStart)
	{
		this.canStart = canStart;
	}


	public boolean isClientExited()
	{
		return clientExited;
	}

	public void setClientExited(boolean clientExited)
	{
		this.clientExited = clientExited;
	}

	//initialize the client
	public void initialize() throws ClassNotFoundException, NullPointerException, IOException, InterruptedException
	{
		initSemaphore.acquire();//acquire semaphore

		//handle object read from the server
		//initialize IOStreams
		this.initializeConnection();

		//start a new thread to continuously listen to the server

		//need to be closed after client terminated
		objectListener = new Thread() {
			public void run() 
			{
				Object objFromServer = null;
				while(!exit)
				{
					if(hasExceptionallyStopped || clientExited) 
					{
						return;
					}
					try 
					{
						//read object from the server through ObjectInputStream
						objFromServer = fromServer.readObject();

						if(objFromServer instanceof InitBean)
						{
							InitBean receivedIBean = (InitBean)objFromServer;//cast to subclass

							//set UUID and isHost to the Player instance
							player.setUUID(receivedIBean.getUUID());
							player.setIsHost(receivedIBean.getIsHost());

							setIsHost(receivedIBean.getIsHost());
							initSemaphore.release();//release semaphore
						}
						else if(objFromServer instanceof PreparedBean) 
						{

							setCanStart(true);

						}
						else if(objFromServer instanceof ExitBean) //server inform that the client should exit
						{	
							if(objFromServer instanceof ExceptionExitBean) 
							{
								setHasExceptionallyStopped(true);
							}
							else
							{
								setClientExited(true);

								//other exit beans send by the server
								//may be end bean to determine the results
							}

							exit=true;
							objectListener.interrupt();//terminates the listener
						}
						else 
						{
							//gameOn objects
							handleGameOnObject(objFromServer);
						}
					}
					catch(ClassNotFoundException e) 
					{

						setHasExceptionallyStopped(true);
						exit=true;
						return;
					}
					catch (NullPointerException e) 
					{
						setHasExceptionallyStopped(true);
						exit=true;
						return;
					}
					catch (IOException e) 
					{
						setHasExceptionallyStopped(true);
						exit=true;
						return;
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		};

		objectListener.start();
	}


	public void setHasInitialized(boolean hasInitialized)
	{
		this.hasInitialized = hasInitialized;
	}

	public boolean getHasInitialized()
	{
		return hasInitialized;
	}

	//initialize socket connection with the server
	private void initializeConnection() throws IOException 
	{
		// Create a socket to connect to the server

		this.socket = new Socket(host, port);
		System.out.println(socket.getPort());
		//System.out.print(socket.getLocalAddress());
		// Create an output stream to send object to the server
		toServer = new ObjectOutputStream(socket.getOutputStream());

		// Create an input stream to receive object from the server
		fromServer = new ObjectInputStream(socket.getInputStream());
	}

	//handle DataBeans to be sent
	private void sendDataBean(DataBean sdBean) 
	{
		try 
		{
			toServer.writeObject(sdBean);
			toServer.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			setHasExceptionallyStopped(true);
		}
	}

	//handle received data bean
	private void handleGameOnObject(Object objFromServer) throws IOException, ClassNotFoundException, NullPointerException, InterruptedException
	{
		if(objFromServer == null) 
		{
			throw new NullPointerException("Read null from the server");
		}
		else
		{
			DataBean receivedBean = (DataBean)objFromServer;

			//polymorphism style of handling handling different events or status
			if (receivedBean instanceof StartBean)
			{
				//when the game starts

				//display("Received Bean is instanceof StartBean");

				this.setHasStarted(true);
				startGame(((StartBean) receivedBean).getMode());
			}
			else if (receivedBean instanceof ResultBean) 
			{
				ResultBean resultBean = (ResultBean)receivedBean;

				//display("Your choice: " + resultBean.getYourChoice().getChoiseName());
				//display("Your opponent's choice: " + resultBean.getOpponentChoice().getChoiseName());

				Integer winOrLose = Integer.valueOf(resultBean.getYourChoice().wins(resultBean.getOpponentChoice()));
				//display results calculated in client
				switch(winOrLose) 
				{
				case 0:
					//display("You Lose");

					break;
				case 1:
					//display("Tie");

					break;
				case 2:
					//display("You Win!");

					break;
				}
				//display("==========");

				//append result for display
				rdp.appendResult(resultBean.getYourChoice(), resultBean.getOpponentChoice(), winOrLose);

				this.setRoundNoInt(Integer.valueOf(resultBean.getRoundNoInt().intValue()+1));//auto boxing?
				//System.out.println(this.getRoundNoInt()+"   hhhhhh");
				//during the game
				if(resultBean.getRoundNoInt().compareTo(modeInt) < 0) 
				{

				}
				else 
				{
					//display("Game over.");
					this.setHasStopped(true);

					//cut off connection to the server
					//display End page
					//restart button? initialize again?
				}
			}
			else
			{
				//when the Bean is non of defined DataBean
				throw new ClassNotFoundException("Undefined Bean");//more custom exceptions comming!
			}
		}
	}

	//the host click on start game button
	public void hostStartGame(int mode) throws InterruptedException 
	{
		if(isCanStart() && getIsHost()) 
		{
			setCanStart(false);
			//display("Host starting game" + "\nBO"+mode);
			sendDataBean(new StartBean(mode));

		}
		else 
		{
			//display("2 people to start");
			return;
		}

	}

	//send the player instance to the server indicates that the game starts
	private void startGame(int mode) 
	{	
		this.setModeInt(mode);
		//display("The game is on!"+"\nBO"+mode);
		this.setRoundNoInt(Integer.valueOf(1));
	}

	//the client made his/her choice
	public void choose(String choiceName) throws ClassNotFoundException 
	{
		if(this.getCanChoose()) 
		{
			this.setMakeChoice(true);
			this.setCanChoose(false);
			ChoiceBean choiceBean = new ChoiceBean(choiceName, player, this.getRoundNoInt());
			sendDataBean(choiceBean);

			//debug
			//display(choiceName);

		}
		else 
		{
			//display("You can't choose now!");
		}

	}

//	//abstract and encapsulate display function
//	private static void display(String string)
//	{
//		System.out.println(string);
//
//		//need to invoke display function of View layer
//	}


	//terminate the client
	public void terminate() 
	{
		if(this.socket != null)
		{
			this.sendDataBean(new ExitBean());
		}


		if(this.objectListener != null) 
		{
			if(objectListener.isAlive())
				objectListener.interrupt();
			else
				objectListener = null;
		}

		if(this.countDownThread != null) 
		{
			if(countDownThread.isAlive())
				countDownThread.interrupt();
			else
				countDownThread = null;
		}
		System.exit(0);

		exit=true;
		//display("The client stoped");
	}
}
