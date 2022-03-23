package rockPaperScissors.rockPaperScissors;

import java.io.Serializable;
import java.net.*;
import java.util.Date;
import java.util.*;
public abstract class DataBean implements Serializable
{	
	private static final long serialVersionUID = 1L;


	protected Date createdDate = null;
	
	//status code
	interface STATUS
	{
		public static final String INIT = "INIT";
		public static final String GAME_START = "GAME_START";
		public static final String GAME_ONMATCH = "GAME_ONMATCH";
		public static final String GAME_END = "GAME_END";
	};

	//constructors
	public DataBean() 
	{
		super();
		this.setCreatedDate(new Date());
	}

	public DataBean(UUID u) 
	{

		this.setCreatedDate(new Date());
	}

	//setters and getters
	
	public DataBean createBean(String status) 
	{
		if(status.equals(STATUS.INIT)) 
		{
			return new InitBean().createBean();
		}
		else if(status.equals(STATUS.GAME_START)) 
		{
			return new StartBean().createBean();
		}
		else if(status.equals(STATUS.GAME_ONMATCH)) 
		{
			return new MatchBean().createBean();
		}
		else
		{
			return new EndBean().createBean();
		}
	}

	public void setCreatedDate(Date d) 
	{
		this.createdDate = d;
	}
	public Date getCreatedDate() 
	{
		return this.createdDate;
	}
	
	
	abstract DataBean createBean();
}
