package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import Model.DataBeans.*;
import Exceptions.*;

//to hold players and match-related classes
public class Room
{
	private Integer roomNoInt = Integer.valueOf(0);
	public Map<UUID, HandleAClient> clientHandlers = new ConcurrentHashMap<UUID, HandleAClient>();
	private final List<ChoiceBean[]> clientChoiceBeans = new ArrayList<ChoiceBean[]>();//results of each round
	private Integer roundNoInt = Integer.valueOf(1);
	Semaphore hostSemaphore = new Semaphore(1);
	//constructors

	public Room() 
	{
		super();
	}

	public Room(Map<UUID, HandleAClient> clientHandlers) 
	{ 

		this.setClientHandlers(clientHandlers);

		for (Entry<UUID, HandleAClient> entry : clientHandlers.entrySet()) 
		{
			entry.getValue().setRoomNo(getRoomNoInt());
		}
	}

	//setters and getters
	public List<ChoiceBean[]> getClientChoiceBeans()
	{
		return clientChoiceBeans;
	}

	public void setClientHandlers(Map<UUID, HandleAClient> clientHandlers) 
	{
		this.clientHandlers = clientHandlers;
	}

	public Map<UUID, HandleAClient> getClientHandlers()
	{
		return this.clientHandlers;
	}

	public void setRoundNoInt(Integer roundNoInt)
	{
		this.roundNoInt = roundNoInt;
	}
	public Integer getRoundNoInt()
	{
		return roundNoInt;
	}
	public void setRoomNoInt(int roomNoInt)
	{
		this.roomNoInt = roomNoInt;
	}
	public Integer getRoomNoInt()
	{
		return roomNoInt;
	}

	public HandleAClient getHostHandler() 
	{
		for (Entry<UUID, HandleAClient> entry : clientHandlers.entrySet()) 
		{
			if(entry.getValue().isHost()) 
			{
				return entry.getValue();
			}
		}

		return null;
	}

	//game on methods
	public void startGame(int m) throws IOException 
	{
		for (Entry<UUID, HandleAClient> entry : clientHandlers.entrySet()) 
		{
			entry.getValue().sendStartBean(m);
		}
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

		HandleAClient player0Handler = clientHandlers.get(player0ChoiceBean.getPlayer().getUUID());
		HandleAClient player1Handler = clientHandlers.get(player1ChoiceBean.getPlayer().getUUID());

		player0Handler.sendResultBean(player0ChoiceBean.getChoice(), player1ChoiceBean.getChoice());
		player1Handler.sendResultBean(player1ChoiceBean.getChoice(), player0ChoiceBean.getChoice());

		setRoundNoInt(Integer.valueOf(getRoundNoInt().intValue() + 1));
	}
}
