package rockPaperScissors.rockPaperScissors;

import java.util.Date;
import java.util.UUID;

public class InitBean extends DataBean
{
	private static final long serialVersionUID = 1L;
	private boolean isHost = false;
	protected UUID uuid = null;//to store UUID for each client to uniquely identify the DataBean is to be sent to which client

	public InitBean()
	{
		super();
	}
	public InitBean(String message, UUID u, String status, boolean isHost) 
	{
		this.message = message;
		this.setCreatedDate(new Date());
		this.setUUID(u);
		this.setStatus(status);
		this.setHost(isHost);
	}

	
	public void setHost(boolean isHost)
	{
		this.isHost = isHost;
	}
	public boolean getIsHost()
	{
		return isHost;
	}
	public void setUUID(UUID u) 
	{
		this.uuid = u;
	}
	public UUID getUUID() 
	{
		return this.uuid;
	}
}
