package ct414;

import java.util.Date;
import java.util.List;

public class MCQ implements Assessment
{
	private int studentid;
	private String information;
	private String courseCode;
	private Date dueDate;
	private List<Question> questions;
	private int[] answers;
	
	public MCQ(String information, String courseCode, Student student, Date dueDate, List<Question> questions)
	{
		studentid = student.getID();
		this.courseCode = courseCode;
		this.dueDate = dueDate;
		this.questions = questions;
		this.information = information;
		
		answers = new int[questions.size()];
	}
	
	// Return information about the assessment
	@Override
	public String getInformation(){
		return information;
	}

	// Return the final date / time for submission of completed assessment
	@Override
	public Date getClosingDate(){
		return dueDate;
	}

	// Return a list of all questions and answer options
	@Override
	public List<Question> getQuestions(){
		return questions;
	}

	// Return one question only with answer options
	@Override
	public Question getQuestion(int questionNumber) throws 
		InvalidQuestionNumber{
		try{
			return questions.get(questionNumber-1);
		}
		catch(ArrayIndexOutOfBoundsException e){
			throw new InvalidQuestionNumber();
		}
	}

	// Answer a particular question
	@Override
	public void selectAnswer(int questionNumber, int optionNumber) throws
		InvalidQuestionNumber, InvalidOptionNumber{
		if(questionNumber < 0 || questionNumber > questions.size())
		{
			throw new InvalidQuestionNumber();
		}
		
		if(optionNumber < 0 || optionNumber > getQuestion(questionNumber).getAnswerOptions().length){
			throw new InvalidOptionNumber();
		}
		answers[questionNumber-1] = optionNumber;
	}

	// Return selected answer or zero if none selected yet
	@Override
	public int getSelectedAnswer(int questionNumber){
		return answers[questionNumber-1];
	}

	// Return studentid associated with this assessment object
	// This will be preset on the server before object is downloaded
	@Override
	public int getAssociatedID(){
		return studentid;
	}
}