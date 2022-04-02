package rockPaperScissors.rockPaperScissors.Exceptions;

public class ChoiceBeforeGameStartException extends Exception
{
	private static final long serialVersionUID = 1L;

	public ChoiceBeforeGameStartException(String errorMessage) 
	{
		super(errorMessage);
	}
}
