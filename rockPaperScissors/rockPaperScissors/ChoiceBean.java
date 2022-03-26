package rockPaperScissors.rockPaperScissors;

//when game has been on
public class ChoiceBean extends DataBean
{
	private static final long serialVersionUID = 1L;
	private Choice choice = new Choice();
	private Player player = null;
	//constructors
	public ChoiceBean() 
	{
		super();
	}
	
	public ChoiceBean(String choiceName, Player player) throws ClassNotFoundException
	{
		this.setPlayer(player);
		choice.setChoiceName(choiceName);
		
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
