package ct414;

import java.util.Date;

public class Session
{
	private Date expiration;
	private int studentid;
	
	public Session(int studentid)
	{
		this.expiration = new Date(System.currentTimeMillis() + (60*60*1000)); // Session expires after an hour
		this.studentid = studentid;
	}
	
	public Date getExpiration()
	{
		return expiration;
	}
	
	public int getStudentid()
	{
		return studentid;
	}
}