package rockPaperScissors.rockPaperScissors;

import java.util.Date;
import java.util.UUID;

public class StartBean extends DataBean
{
	private static final long serialVersionUID = 1L;
	private Player player = null;
	public StartBean() 
	{
		super();
	}
	public StartBean(String message, String status, Player player)
	{
		this.message = message;
		this.setCreatedDate(new Date());
		this.setStatus(status);
		this.setPlayer(player);
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	public Player getPlayer()
	{
		return player;
	}
}
