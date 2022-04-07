package rockPaperScissors.rockPaperScissors.DataBeans;

import javafx.scene.image.Image;
import rockPaperScissors.rockPaperScissors.Choice;

public class ResultBean extends DataBean
{

	private static final long serialVersionUID = 1L;
	private Choice yourChoice = null;
	private Choice opponentChoice = null;
	private Integer roundNoInt;
	public ResultBean() 
	{
		super();
	}
	
	public ResultBean(Choice yourChoice, Choice opponentChoice, Integer roundNoInt) 
	{
		this.setYourChoice(yourChoice);
		this.setOpponentChoice(opponentChoice);
		this.setRoundNoInt(roundNoInt);
	}
	public void setYourChoice(Choice yourChoice)
	{
		this.yourChoice = yourChoice;
	}
	public Choice getYourChoice()
	{
		return yourChoice;
	}
	
	public void setOpponentChoice(Choice opponentChoice)
	{
		this.opponentChoice = opponentChoice;
	}
	public Choice getOpponentChoice()
	{
		return opponentChoice;
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
