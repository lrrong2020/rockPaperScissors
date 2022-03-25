package rockPaperScissors.rockPaperScissors;

//when game has been on
public class GameOnBean extends DataBean
{
	private static final long serialVersionUID = 1L;
	private Choice choice = null;
	private Player player = null;
	//constructors
	public GameOnBean() 
	{
		super();
	}
	
	public GameOnBean(String choiseName, Player player) throws ClassNotFoundException
	{
		this.setPlayer(player);
		if(choiseName.equals(Choice.GESTURES.ROCK) || choiseName.equals(Choice.GESTURES.PAPER)) 
		{
			choice.setChoiseName(choiseName);
		}
		else 
		{
			throw new ClassNotFoundException("No such choice");
		}
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

	public void setChoice(Choice choice) 
	{
		this.choice = choice;
	}
	public Choice getChoice() 
	{
		return this.choice;
	}

}
