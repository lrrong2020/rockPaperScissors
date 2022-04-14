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

	
	//constructors
	public Choice() 
	{
		super();
	}
	
	public Choice(String s) throws ClassNotFoundException 
	{
		this.setChoiceName(s);
	}
	
	//setters and getters
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

	public int wins(Choice opponentChoice) {
		if(opponentChoice.getChoiseName().equals(GESTURES.ROCK)) 
		{
			if(this.choiceName.equals(GESTURES.PAPER)) return 2;
			else if(this.choiceName.equals(GESTURES.SCISSORS)) return 0;
			else return 1;
		}
		

		else if(opponentChoice.getChoiseName().equals(GESTURES.PAPER)) 
		{

			if(this.choiceName.equals(GESTURES.SCISSORS)) return 2;
			else if(this.choiceName.equals(GESTURES.ROCK)) return 0;
			else return 1;
		}
		
		else if(opponentChoice.getChoiseName().equals(GESTURES.SCISSORS))
		{

			if(this.choiceName.equals(GESTURES.ROCK)) return 2;
			else if(this.choiceName.equals(GESTURES.PAPER)) return 0;
			else return 1;
		}
		else 
		{
			return -1;
		}
	}
	
	public boolean equals(Object o) 
	{
		if(o instanceof Choice) 
		{
			Choice theChoice = (Choice) o;
			if(theChoice.getChoiseName().equals(this.getChoiseName())) 
			{
				return true;
			}
			else 
			{
				return false;
			}
		}
		return false;
	}
}
