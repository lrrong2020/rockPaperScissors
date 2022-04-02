package rockPaperScissors.rockPaperScissors;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import rockPaperScissors.rockPaperScissors.DataBeans.*;
import rockPaperScissors.rockPaperScissors.Exceptions.ChoiceMoreThanOnceException;

//to hold players and match-related classes
public class Room
{
	private int roomNo = 0;
	private Map<UUID, Socket> users = new ConcurrentHashMap<UUID, Socket>();
	private List<HandleAClient> clientHandlers = new ArrayList<HandleAClient>();//list of 
	private final List<ChoiceBean[]> clientChoiceBeans = new ArrayList<ChoiceBean[]>();//results of each round
	private int roundNo = 1;
	//constructors
	
	public Room() 
	{
		super();
	}
	
	public Room(Map<UUID, Socket> users, List<HandleAClient> clientHandlers) 
	{ 
		this.setUsers(users);
		this.setClientHandlers(clientHandlers);
	}
	
	//setters and getters
	public List<ChoiceBean[]> getClientChoiceBeans()
	{
		return clientChoiceBeans;
	}

	
	public void sendResults(int rNoI0) throws ClassNotFoundException, IOException, ChoiceMoreThanOnceException 
	{
		ChoiceBean[] choiceBeanArr = this.clientChoiceBeans.get(rNoI0);//0

		ChoiceBean player0ChoiceBean = choiceBeanArr[0];
		ChoiceBean player1ChoiceBean = choiceBeanArr[1];

		if(player0ChoiceBean.equals(player1ChoiceBean)) 
		{
			throw new ChoiceMoreThanOnceException("Write 2 times");
		}

		Socket player0Socket = users.get(player0ChoiceBean.getPlayer().getUUID());

		HandleAClient player0Handler = getClientHandler(player0Socket);
		HandleAClient player1Handler = null;

		//find the HandleAClient instance that matches

		//need to control access
		if(player0Socket.equals(getClientHandlers().get(0).getSocket())) 
		{
			player0Handler = getClientHandlers().get(0);
			player1Handler = getClientHandlers().get(1);//get another
		}
		else if(player0Socket.equals(getClientHandlers().get(1).getSocket())) 
		{
			player0Handler = getClientHandlers().get(1);
			player1Handler = getClientHandlers().get(0);//get another
		}
		else throw new ClassNotFoundException("Socket not found");


		System.out.print(player0ChoiceBean.getChoice().toString());
		System.out.print(player1ChoiceBean.getChoice().toString());

		player0Handler.sendResultBean(player0ChoiceBean.getChoice(), player1ChoiceBean.getChoice());
		player1Handler.sendResultBean(player1ChoiceBean.getChoice(), player0ChoiceBean.getChoice());

		setRoundNo(getRoundNo() + 1);
	}

	public void setUsers(Map<UUID, Socket> users) 
	{
		this.users = users;
	}
	public Map<UUID, Socket> getUsers()
	{
		return this.users;
	}

	public void setClientHandlers(List<HandleAClient> clientHandlers)
	{
		this.clientHandlers = clientHandlers;
	}
	public List<HandleAClient> getClientHandlers()
	{
		return clientHandlers;
	}
	public HandleAClient getClientHandler(Socket socket) 
	{
		for (HandleAClient clientHandler: this.getClientHandlers()) 
		{
			if(clientHandler.getSocket().equals(socket))
			{
				return clientHandler;
			}
		}
		return null;
	}
	
	public void setRoundNo(int roundNo)
	{
		this.roundNo = roundNo;
	}
	public int getRoundNo()
	{
		return roundNo;
	}
	public void setRoomNo(int roomNo)
	{
		this.roomNo = roomNo;
	}
	public int getRoomNo()
	{
		return roomNo;
	}


}
