package rockPaperScissors.rockPaperScissors.DataBeans;

import java.util.Date;

import rockPaperScissors.rockPaperScissors.Player;

public class StartBean extends DataBean
{
	private static final long serialVersionUID = 1L;
	private Player player = null;//to hold player instance
	
	//constructors
	public StartBean() 
	{
		super();
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
}
