package rockPaperScissors.rockPaperScissors;

import java.io.Serializable;
import java.net.*;
import java.util.Date;
import java.util.*;
public class DataBean implements Serializable
{	
	private static final long serialVersionUID = 1L;

	protected String message = null;
	protected Date createdDate = null;
	

	private String status = null;
	//status code
	interface STATUS
	{
		public static final String INIT = "INIT";
		public static final String GAME_START = "GAME_START";
		public static final String GAME_ONMARCH = "GAME_ONMARCH";
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
	}

	public DataBean(String message, String status) 
	{
		this.message = message;
		this.setCreatedDate(new Date());

		this.setStatus(status);
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
	
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
}
