package Model.DataBeans;

public class ExitBean extends DataBean
{
	private static final long serialVersionUID = 1L;
	private Exception exception = null;

	public ExitBean() 
	{
		super();
	}

	public ExitBean(Exception exception) 
	{
		this.setException(exception);
	}

	public Exception getException() 
	{
		return exception;
	}

	public void setException(Exception exception) 
	{
		this.exception = exception;
	}	
}
