package rockPaperScissors.rockPaperScissors;

public class ResultBean extends DataBean
{

	private static final long serialVersionUID = 1L;
	private Choice yourChoice = null;
	private Choice opponentChoice = null;
	public ResultBean() 
	{
		super();
	}
	
	public ResultBean(Choice yourChoice, Choice opponentChoice) 
	{
		this.setYourChoice(yourChoice);
		this.setOpponentChoice(opponentChoice);
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

}
