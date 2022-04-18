package rockPaperScissors.Model;

import java.io.*;
import java.util.UUID;

/** should not use singleton for player because the server need to hold more than 1 player instances **/
public class Player implements Serializable
{
	private static final long serialVersionUID = 1L;
	private UUID uuid = null;//uniquely identify a player
	private boolean isHost = false;//stored in player to identify if a client is a host conveniently

	//constructors
	public Player() 
	{
		super();
	}

	//setters and getters
	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
	}
	public UUID getUUID()
	{
		return this.uuid;
	}
	public void setIsHost(boolean isHost)
	{
		this.isHost = isHost;
	}
	public boolean getIsHost()
	{
		return this.isHost;
	}
}
