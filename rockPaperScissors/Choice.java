package rockPaperScissors.rockPaperScissors;

import java.io.Serializable;

public class Choice implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String choiceName = null;
	
	public interface GESTURES
	{
		String ROCK = "ROCK";
		String PAPER = "PAPER";
		String SCISSORS = "SCISSORS";
	}
	
	public Choice() 
	{
		super();
	}
	
	public Choice(String s) throws ClassNotFoundException 
	{
		this.setChoiceName(s);
	}
	
	public void setChoiceName(String choiceName) throws ClassNotFoundException
	{
		if(choiceName.equals(GESTURES.ROCK) || choiceName.equals(GESTURES.PAPER) || choiceName.equals(GESTURES.SCISSORS))
		{
			this.choiceName = choiceName;
		}
		else 
		{
			throw new ClassNotFoundException();
		}

	}
	public String getChoiseName()
	{
		return choiceName;
	}
	
	public static void main(String[] args) throws ClassNotFoundException 
	{
		Choice choice1 = new Choice("PAPER");
		Choice choice2 = new Choice(GESTURES.ROCK);
		System.out.println(choice1.wins(choice2));
		
	}
	
	public int wins(Choice opponentChoice) {
		if(opponentChoice.getChoiseName().equals(GESTURES.ROCK)) 
		{
			if(this.choiceName == GESTURES.PAPER) return 2;
			else if(this.choiceName == GESTURES.SCISSORS) return 0;
			else return 1;
		}
		
		else if(opponentChoice.getChoiseName().equals(GESTURES.PAPER)) 
		{
			if(this.choiceName == GESTURES.SCISSORS) return 2;
			else if(this.choiceName == GESTURES.ROCK) return 0;
			else return 1;
		}
		
		else if(opponentChoice.getChoiseName().equals(GESTURES.SCISSORS))
		{
			if(this.choiceName == GESTURES.ROCK) return 2;
			else if(this.choiceName == GESTURES.PAPER) return 0;
			else return 1;
		}
		else 
		{
			return -1;
		}
	}
}
