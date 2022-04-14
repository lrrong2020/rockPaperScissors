package rockPaperScissors.rockPaperScissors.Exceptions;

public class OpponentExitException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public OpponentExitException(String errorMessage) 
	{
		super(errorMessage);
	}
}
