package rockPaperScissors.rockPaperScissors.DataBeans;

public class PreparedBean extends DataBean
{
	private static final long serialVersionUID = 1L;
	private int roomNo = 0;
	
	public PreparedBean() 
	{
		super();
	}
	
	public PreparedBean(int roomNo) 
	{
		super();
		setRoomNo(roomNo);
	}
	
	public int getRoomNo()
	{
		return roomNo;
	}
	public void setRoomNo(int roomNo)
	{
		this.roomNo = roomNo;
	}
}
