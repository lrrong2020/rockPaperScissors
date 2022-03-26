package rockPaperScissors.rockPaperScissors;

public class ResultBean extends DataBean
{

	private static final long serialVersionUID = 1L;
	private Choice yourChoice = null;
	private Choice opponentChoice = null;
	private int result = -1;
	public ResultBean() 
	{
		super();
	}
	
	public ResultBean(Choice yourChoice, Choice opponentChoice, int result) 
	{
		this.setYourChoice(yourChoice);
		this.setOpponentChoice(opponentChoice);
		this.setResult(result);
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

	public int getResult()
	{
		return result;
	}

	public void setResult(int result)
	{
		this.result = result;
	}
}
