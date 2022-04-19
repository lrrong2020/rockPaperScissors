package Exceptions;

public class DataInconsistentException extends Exception
{
	private static final long serialVersionUID = 1L;

	public DataInconsistentException(String errorMessage) 
	{
		super(errorMessage);
	}
}
