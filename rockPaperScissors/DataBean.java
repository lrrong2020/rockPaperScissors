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
//	interface STATUS
//	{
//		public static final String INIT = "INIT";
//		public static final String GAME_START = "GAME_START";
//		public static final String GAME_ONMATCH = "GAME_ONMATCH";
//		public static final String GAME_END = "GAME_END";
//	};

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
	public void setCreatedDate(Date d) 
	{
		this.createdDate = d;
	}
	public Date getCreatedDate() 
	{
		return this.createdDate;
	}
}
