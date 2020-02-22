
package ct414;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ExamEngine implements ExamServer {

	private Student[] students;
	private Assessment[] assessments;
	private HashMap<Integer, Session> activeSessions;

    // Constructor is required
    public ExamEngine(Student[] students, Assessment[] assessments) {
        super();
		this.students = students;
		this.assessments = assessments;
		this.activeSessions = new HashMap<>();
    }

    // Implement the methods defined in the ExamServer interface...
    // Return an access token that allows access to the server for some time period
    public int login(int studentid, String password) throws 
                UnauthorizedAccess, RemoteException {
		for(Student s: students)
		{
			if(s.getID() == studentid && s.getPassword().equals(password))
			{
				int token = new Random().nextInt(Integer.MAX_VALUE);
				activeSessions.put(token, new Session(studentid));
				return token;
			}
		}
		
		throw new UnauthorizedAccess ("Incorrect Username or Password");
    }

    // Return a summary list of Assessments currently available for this studentid
    public List<String> getAvailableSummary(int token, int studentid) throws
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {

		authoriseToken(token, studentid); // Will handle unauthorised access exceptions
		
		List<String> validAssessments = new ArrayList<>();
		
		for(Assessment a: assessments)
		{
			if(a.getAssociatedID() == studentid)
			{
				validAssessments.add(a.getInformation());
			}
		}
		
		if(validAssessments.isEmpty())
		{
			throw new NoMatchingAssessment("No assessments");
		}

        return validAssessments;
    }

    // Return an Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseName) throws
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        authoriseToken(token, studentid); // Will handle unauthorised access exceptions
		
		for(Assessment a: assessments)
		{
			if(a.getInformation().equals(courseName) && a.getAssociatedID() == studentid && a.getClosingDate().after(new Date()))
			{
				return a;
			}
		}
		
		throw new NoMatchingAssessment("No mathcing assessment found");
    }

    // Submit a completed assessment
    public void submitAssessment(int token, int studentid, Assessment completed) throws 
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        authoriseToken(token, studentid); // Will handle unauthorised access exceptions
		
		for(Assessment a: assessments)
		{
			if(a.getInformation().equals(completed.getInformation()) && a.getAssociatedID() == studentid && a.getClosingDate().after(new Date()))
			{
				a = completed;
				return;
			}
		}
		
		throw new NoMatchingAssessment("No matching assessment found");
    }
	
	public void authoriseToken(int token, int studentid) throws UnauthorizedAccess, RemoteException
	{
		Session session = activeSessions.get(token);
		
		if(session == null || session.getStudentid() != studentid)
		{
			throw new UnauthorizedAccess("Please log in");
		}
		
		if(session.getExpiration().before(new Date()))
		{
			activeSessions.remove(token);
			throw new UnauthorizedAccess("Session Expired");
		}
	}

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
			Student student = new Student(1, "pasword123");
			Student student2 = new Student(2, "pass");
			
			List<Question> historyQuestions = new ArrayList<>();
			Question q1 = new MCQquestion("What year was the Easter Rising?", new String[]{"2010", "1917", "1916"}, 1);
			Question q2 = new MCQquestion("Who was the first President of the United States?", new String[]{"George Washington", "George Michael", "Curious George"}, 2);
			Question q3 = new MCQquestion("Where did Christopher Columbus set sail from?", new String[]{"Italy", "Spain", "France"}, 3);
			
			historyQuestions.add(q1);
			historyQuestions.add(q2);
			historyQuestions.add(q3);
			
			Assessment mcq = new MCQ("History", "hist01", student, new Date(System.currentTimeMillis() + 14*24*3600*1000), historyQuestions);
			
			Assessment mcq2 = new MCQ("Maths", "maths01", student, new Date(System.currentTimeMillis() + 14*24*3600*1000), 
			Arrays.asList(new Question[]{
				new MCQquestion("2 + 2 = ?", new String[]{"4", "7", "3"}, 1),
				new MCQquestion("2 X 2 = ?", new String[]{"5", "4", "6"}, 2),
				new MCQquestion("2 - 2 = ?", new String[]{"2", "0", "-4"}, 3)
			}));
			
			Assessment[] assessments = new Assessment[]{mcq, mcq2};
			
            String name = "ExamServer";
            Student[] students = new Student[]{student, student2};
            ExamServer engine = new ExamEngine(students, assessments);
			
            ExamServer stub =
                (ExamServer) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(name, stub);
            System.out.println("ExamEngine bound");
        } catch (Exception e) {
            System.err.println("ExamEngine exception:");
            e.printStackTrace();
        }
    }
}
