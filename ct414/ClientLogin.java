package ct414;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientLogin extends JFrame
{
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JTextField username;
	private JPasswordField password;
	private JButton login;
	static ExamServer examServer;
	private JLabel label4;
	private Container contentPane;
	
	public ClientLogin(ExamServer examServer)
	{
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		label4 = new JLabel();
		this.examServer = examServer;
		
		username = new JTextField();
		password = new JPasswordField();
		login = new JButton();
		
		Container contentPane = getContentPane();
		
		label1.setText("Username:");
		label2.setText("Password:");
		//label1.setText("Username:");
		
		login.setText("Login");
		login.addActionListener(e -> loginButtonActionPerformed(e));
		
		JPanel panel1 = new JPanel(new GridLayout(2,1,2,2));
		panel1.add(label1);
		panel1.add(username);
		panel1.add(label2);
		panel1.add(password);
		JPanel panel2 = new JPanel(new FlowLayout());
		panel2.add(login);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(panel1, BorderLayout.NORTH);
		add(panel2, BorderLayout.SOUTH);
		setTitle("Login");
		setSize(450,250);
		setVisible(true);
	}
	
	private void loginButtonActionPerformed(ActionEvent e)
	{
		int usernameValue = Integer.valueOf(username.getText());
		char[] passwordChar = password.getPassword();
		String passwordValue = String.copyValueOf(passwordChar);
		
		try{
            int token = this.examServer.login(usernameValue,passwordValue);
            setVisible(false);
            AssessmentsList assessmentsList = new AssessmentsList(examServer, usernameValue, token);
        }
        catch (UnauthorizedAccess unauthorizedAccess){
            System.err.println("Incorrect Username or Password");
			JOptionPane.showMessageDialog(contentPane, "Invalid Login");
        }
        catch (RemoteException remoteException){
            System.err.println("Remote Exception");
            remoteException.printStackTrace();
        }
	}
	
	public static void main(String[] args)
	{
		if(System.getSecurityManager() == null)
		{
			System.setSecurityManager(new SecurityManager());
		}
		try {
            String name = "ExamServer";
            Registry registry = LocateRegistry.getRegistry();
            examServer = (ExamServer) registry.lookup(name);
            ClientLogin clientLogin = new ClientLogin(examServer);

        } catch (Exception e) {
            System.err.println("StudentLogin exception:");
            e.printStackTrace();
        }
	}
}