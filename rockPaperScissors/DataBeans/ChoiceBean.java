package rockPaperScissors.rockPaperScissors.DataBeans;

import rockPaperScissors.rockPaperScissors.Choice;
import rockPaperScissors.rockPaperScissors.Player;

//when game has been on
public class ChoiceBean extends DataBean
{
	private static final long serialVersionUID = 1L;
	
	private Choice choice = new Choice();
	private Player player = null;
	private Integer roundNoInt = Integer.valueOf(0);
	
	//constructors
	public ChoiceBean() 
	{
		super();
	}
	
	public ChoiceBean(String choiceName, Player player, Integer roundNoInt) throws ClassNotFoundException
	{
		this.setPlayer(player);
		this.setRoundNoInt(roundNoInt);

		if(choiceName.equals(Choice.GESTURES.ROCK) || choiceName.equals(Choice.GESTURES.PAPER) || choiceName.equals(Choice.GESTURES.SCISSORS)) 
		{
			this.setChoice(new Choice(choiceName));
			choice.setChoiceName(choiceName);
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
	
	public String toString() 
	{
		return "Player: "+this.player.getUUID() + "\nChoice:" + this.choice.getChoiseName() + "\nroundNo: " + this.roundNoInt.toString();
	}

	public void setRoundNoInt(Integer roundNoInt)
	{
		this.roundNoInt = roundNoInt;
	}
	public Integer getRoundNoInt()
	{
		return roundNoInt;
	}
}
