package ct414;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;

public class AssessmentsList extends JFrame{
	
	static int studentid;
	List<Assessment> assessmentsList = new ArrayList<>();
	ExamServer examServer;
	int token;
	Assessment currentAssessment;
	List<String> courseCodes;
	
	private JLabel studentIdLabel, availableAssessments, selectSubject, assessmentSubject, assessmentDue;
    private JPanel panel1;
    private JComboBox subjectSelectionBox;
    private JButton start;


	public AssessmentsList(ExamServer examServer, int studentid, int token){
		this.studentid = studentid;
		this.examServer = examServer;
		this.token = token;
		boolean assessmentNotAvailable = false;
		Container contentPane = getContentPane();
		
		try{
			courseCodes = examServer.getAvailableSummary(token, studentid);
			for(String s: courseCodes)
			{
				assessmentsList.add(examServer.getAssessment(token, studentid, s));
			}
		}
        catch (NoMatchingAssessment noMatchingAssessment){
            assessmentNotAvailable = true;
            System.err.println("No Assessment Available");
        }
        catch (RemoteException remoteException){
            System.err.println("Remote Exception");
            remoteException.printStackTrace();
        }
        catch (UnauthorizedAccess unauthorizedAccess){
            System.err.println("Unauthorised Access");
			JOptionPane.showMessageDialog(contentPane, "Session has expired");
			ClientLogin clientLogin = new ClientLogin(examServer);
        }
		
		studentIdLabel = new JLabel();
		availableAssessments = new JLabel();
		assessmentSubject = new JLabel();
        assessmentDue = new JLabel();
        panel1 = new JPanel();
        selectSubject = new JLabel();
        subjectSelectionBox = new JComboBox();
        start = new JButton();

        setTitle("Assessments Available");

        studentIdLabel.setText("Student ID: " + studentid);

        availableAssessments.setText("You Have No Assessments Due");

        if(!assessmentNotAvailable) {
			availableAssessments.setText("Assignments Available: " + assessmentsList.size());

			selectSubject.setText("Select from: ");

			currentAssessment = assessmentsList.get(0);

			for (Assessment a: assessmentsList) {
				subjectSelectionBox.addItem(a.getInformation());
			}

			subjectSelectionBox.addItemListener(new ItemChangeListener());

			assessmentSubject.setText(currentAssessment.getInformation());

			assessmentDue.setText("Due: " + currentAssessment.getClosingDate());

			start.setText("Start Assignment");
			start.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					AssessmentClient assessmentClient = new AssessmentClient(examServer, token, studentid, currentAssessment);
				}
			});

		}
		
		if(assessmentNotAvailable){
			subjectSelectionBox.setVisible(false);
			start.setVisible(false);
		}
		
		JPanel panel1 = new JPanel(new GridLayout(3,1));
		JPanel panel2 = new JPanel(new FlowLayout());
		panel1.add(studentIdLabel);
		panel1.add(availableAssessments);
		panel1.add(selectSubject);
		panel1.add(subjectSelectionBox);
		panel1.add(assessmentSubject);
		panel1.add(assessmentDue);
		
		panel2.add(start);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(panel1,BorderLayout.NORTH);
		add(panel2,BorderLayout.SOUTH);
		setTitle("List of available assignments");
		setSize(400,400);
		setVisible(true);
	}
	
	class ItemChangeListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                currentAssessment = assessmentsList.get(subjectSelectionBox.getSelectedIndex());
                assessmentSubject.setText(currentAssessment.getInformation());
                assessmentDue.setText("Due: " + currentAssessment.getClosingDate());
            }
        }
    }
}