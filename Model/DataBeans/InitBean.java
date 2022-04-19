package Model.DataBeans;

import java.util.Date;
import java.util.UUID;

public class InitBean extends DataBean
{
	private static final long serialVersionUID = 1L;
	private boolean isHost = false;//identify if the user is the host and it's encapsulated in Player instance
	protected UUID uuid = null;//to store UUID for each client to uniquely identify the DataBean is to be sent to which client

	//constructors
	public InitBean()
	{
		super();
	}
	public InitBean(UUID u, boolean isHost) 
	{
		this.setCreatedDate(new Date());
		this.setUUID(u);
		this.setHost(isHost);
	}

	//setters and getters
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
