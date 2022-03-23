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
	public StartBean(String status, Player player)
	{

		this.setCreatedDate(new Date());
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
	@Override
	DataBean createBean()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
