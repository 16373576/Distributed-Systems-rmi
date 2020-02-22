package ct414;

public class Student {
	
	private int id;
	private String password;
	
	public Student(int id, String password)
	{
		this.id = id;
		this.password = password;
	}
	
	public int getID()
	{
		return id;
	}
	
	public String getPassword()
	{
		return password;
	}
}