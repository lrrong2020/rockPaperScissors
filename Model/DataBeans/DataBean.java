package Model.DataBeans;

import java.io.Serializable;
import java.util.Date;

public abstract class DataBean implements Serializable//implements Serializable to transmit through objectStream
{	
	private static final long serialVersionUID = 1L;
	protected Date createdDate = null;

	//constructors
	public DataBean()
	{
		super();
		this.setCreatedDate(new Date());
	}

	//setters and getters
	public void setCreatedDate(Date d)
	{
		this.createdDate = d;
	}
	public Date getCreatedDate()
	{
		return this.createdDate;
	}
}
