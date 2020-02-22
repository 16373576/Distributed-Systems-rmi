package ct414;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ct414.AssessmentsList.ItemChangeListener;

public class AssessmentClient extends JFrame
{
	Assessment assessment;
	ExamServer es;
	int token;
	int idNum;
	Question selectedQuestion;
	
	public AssessmentClient(ExamServer server, int token, int username, Assessment assessment)
	{
		es = server;
		this.assessment = assessment;
		this.token = token;
		idNum = username;
		
		try
		{
			selectedQuestion = assessment.getQuestion(1);
			initComponents();
		}
		catch(InvalidQuestionNumber invalidQuestionNumber)
		{
			System.err.println("Invalid Question Number");
			invalidQuestionNumber.printStackTrace();
		}
	}
	
	private JLabel dueDate;
    private JLabel assessmentInfo;
    private JComboBox questionSelect;
    private JLabel questionText;
    private JLabel questionSelectLabel;
    private JComboBox answerSelect;
    private JLabel answerSelectLabel;
    private JLabel questionLabel;
    private JButton submit;
	
	public void initComponents()
	{
		dueDate = new JLabel();
		assessmentInfo = new JLabel();
		questionSelect = new JComboBox();
		answerSelect = new JComboBox();
		questionSelectLabel = new JLabel();
		questionText = new JLabel();
		answerSelectLabel = new JLabel();
		questionLabel = new JLabel();
		submit = new JButton();
		
		setTitle(assessment.getInformation());
		
		Container contentPane = getContentPane();
		
		dueDate.setText("Due: " + assessment.getClosingDate());
		
		assessmentInfo.setText(assessment.getQuestions().size() + " questions");
		
		questionSelectLabel.setText("Current Question: ");
		
		questionText.setText(selectedQuestion.getQuestionDetail());
		
		// adding question numbers to question select box
		for(int i = 1; i <= assessment.getQuestions().size(); i++)
		{
			questionSelect.addItem(i);
		}
		
		for(int j = 0; j < selectedQuestion.getAnswerOptions().length; j++)
		{
			answerSelect.addItem(selectedQuestion.getAnswerOptions()[j]);
		}
		
		questionSelect.addItemListener(new ItemChangeListener());
		
		questionLabel.setText("Question: ");
		
		answerSelectLabel.setText("Select answer: ");
		
		submit.setText("Submit Assessment");
		submit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					assessment.selectAnswer(questionSelect.getSelectedIndex()+1, answerSelect.getSelectedIndex());
					es.submitAssessment(token, idNum, assessment);
					setVisible(false);
					
					JOptionPane.showMessageDialog(contentPane, "Assessment submitted");
					AssessmentsList assessmentsList = new AssessmentsList(es, idNum, token);
				}
				catch (InvalidQuestionNumber invalidQuestionNumber){
                    invalidQuestionNumber.printStackTrace();
                    System.err.println("Invalid Question Number");
                }catch (InvalidOptionNumber invalidOptionNumber){
                    invalidOptionNumber.printStackTrace();
                    System.err.println("Invalid Question Number");
                }catch (UnauthorizedAccess unauthorizedAccess){
                    System.err.println("Unauthorized Access");
					JOptionPane.showMessageDialog(contentPane, "Session has expired");
					ClientLogin clientLogin = new ClientLogin(es);
                }catch (RemoteException remoteException){
                    remoteException.printStackTrace();
                    System.err.println("Remote Exception");
                }catch (NoMatchingAssessment noMatchingAssessment){
                    noMatchingAssessment.printStackTrace();
                    System.err.println("No Matching Assessment");
                }
			}
		});
		
		JPanel panel1 = new JPanel(new GridLayout(4,1));
		JPanel panel2 = new JPanel(new FlowLayout());
		
		panel1.add(assessmentInfo);
		panel1.add(dueDate);
		panel1.add(questionSelectLabel);
		panel1.add(questionSelect);
		panel1.add(questionLabel);
		panel1.add(questionText);
		panel1.add(answerSelectLabel);
		panel1.add(answerSelect);
		
		panel2.add(submit);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(panel1,BorderLayout.NORTH);
		add(panel2,BorderLayout.SOUTH);
		setTitle("Assesment");
		setSize(600,300);
		setVisible(true);
	}
	
	class ItemChangeListener implements ItemListener {
        @Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
                //as we are selecting from the combo box, the user cannot type in invalid question number
                try {
                    selectedQuestion = assessment.getQuestion(questionSelect.getSelectedIndex() + 1);
                    questionText.setText(selectedQuestion.getQuestionDetail());
                    answerSelect.removeAllItems();
                    for(int j = 0;j<selectedQuestion.getAnswerOptions().length; j++){
                        answerSelect.addItem(selectedQuestion.getAnswerOptions()[j]);
                    }
                }catch (InvalidQuestionNumber invalidQuestionNumber){
                    System.err.println("Invalid Question Number");
                    invalidQuestionNumber.printStackTrace();
                }
            }
		}
    }
}