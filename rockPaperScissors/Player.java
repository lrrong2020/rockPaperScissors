package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.util.UUID;

/*Still confused about if I should use class-level varibale or singleton to hold the player instance*/

public class Player implements Serializable
{
	private static final long serialVersionUID = 1L;

	private static Player instance = new Player();
	private UUID uuid = null;
	private boolean isHost = false;


	private Player() 
	{
		super();
	}

	public static Player getInstance() 
	{
		return Player.instance;
	}
	public UUID getUUID()
	{
		return this.uuid;
	}
	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
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
