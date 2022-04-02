package rockPaperScissors.rockPaperScissors.DataBeans;

import java.util.Date;

import rockPaperScissors.rockPaperScissors.Player;

public class StartBean extends DataBean
{
	private static final long serialVersionUID = 1L;
	private Player player = null;//to hold player instance
	private int mode = 0;//
	private int roomNo = 0;
	
	//constructors
	public StartBean() 
	{
		super();
	}
	
	public StartBean(int m) 
	{
		super();
		this.setMode(m);
	}
	
	public StartBean(Player player)
	{
		this.setCreatedDate(new Date());
		this.setPlayer(player);
	}
	
	//setters and getters
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	public Player getPlayer()
	{
		return player;
	}
	
	public void setMode(int mode)
	{
		this.mode = mode;
	}
	public int getMode()
	{
		return mode;
	}

	public void setRoomNo(int roomNo)
	{
		this.roomNo = roomNo;
	}
	public int getRoomNo()
	{
		return roomNo;
	}
}
