package rockPaperScissors.rockPaperScissors;

import java.io.Serializable;
import java.net.*;
import java.util.Date;
import java.util.stream.Stream;
import java.util.*;
public class DataBean implements Serializable
{	
	private static final long serialVersionUID = 1L;
	private String message = null;
	private Date createdDate = null;
	private UUID uuid = null;//to store UUID for each client to uniquely identify the DataBean is to be sent to which client
	private Map<UUID, Socket> ONLINE_USER_MAP = null;//for storing users
	private String status = null;
	//status code
	class STATUS
	{
		public static final String INIT = "INIT";
		public static final String GAME_START = "GAME_START";
		public static final String GAME_END = "GAME_END";
	};

	//constructors
	public DataBean() 
	{
		super();
		this.setCreatedDate(new Date());
	}
	public DataBean(String str) 
	{
		this.message = str;
		this.setCreatedDate(new Date());
	}
	public DataBean(String str, UUID u) 
	{
		this.message = str;
		this.setCreatedDate(new Date());
		this.setUUID(u);
	}
	
	//setters and getters
	public void setMessage(String str) 
	{
		this.message = str;
	}
	public String getMessage() 
	{
		return this.message;
	}

	public void setCreatedDate(Date d) 
	{
		this.createdDate = d;
	}
	public Date getCreatedDate() 
	{
		return this.createdDate;
	}

	public void setUserMap(Map<UUID, Socket> m) 
	{
		this.ONLINE_USER_MAP= m;
	}
	public Map<UUID, Socket> getUserMap() 
	{
		return this.ONLINE_USER_MAP;
	}

	public void setUUID(UUID u) 
	{
		this.uuid = u;
	}
	public UUID getUUID() 
	{
		return this.uuid;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getStatus()
	{
		return status;
	}
}
