package rockPaperScissors.rockPaperScissors;

import java.io.*;
import java.util.UUID;

public class Player implements Serializable
{
	private static final long serialVersionUID = 1L;

	private static Player instance = new Player();
	private UUID uuid = null;


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

}
