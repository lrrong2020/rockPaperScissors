package rockPaperScissors.rockPaperScissors;

public class Choice
{
	private String choiseName = null;
	public interface GESTURES
	{
		String ROCK = "ROCK";
		String PAPER = "PAPER";
		String SCISSORS = "SCISSORS";
	}
	
	public void setChoiseName(String choiseName)
	{
		this.choiseName = choiseName;
	}
	public String getChoiseName()
	{
		return choiseName;
	}
}
