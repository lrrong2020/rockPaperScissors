package rockPaperScissors.rockPaperScissors.Exceptions;

public class ChoiceMoreThanOnceException extends Exception
{
	private static final long serialVersionUID = 1L;

	public ChoiceMoreThanOnceException(String errorMessage) 
	{
		super(errorMessage);
	}

}
