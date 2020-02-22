package ct414;

public class MCQquestion implements Question
{
	private String details;
	private int number;
	private String[] answerOptions;
	
	public MCQquestion(String details, String[] answerOptions, int number)
	{
		this.details = details;
		this.number = number;
		this.answerOptions = answerOptions;
	}
	
	// Return the question number
	@Override
	public int getQuestionNumber()
	{
		return number;
	}

	// Return the question text
	@Override
	public String getQuestionDetail()
	{
		return details;
	}

	// Return the possible answers to select from
	@Override
	public String[] getAnswerOptions()
	{
		return answerOptions;
	}
}