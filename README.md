# Distributed-Systems-rmi
Implementation of a java RMI assessment system, which has a server that allows clients to authenticate and download Assessment objects

How to run our Assessment System:
1.	Update security.policy so that it contains the correct path
2.	Ensure that the jdk is added to the path
3.	Compile the java files:
    javac ct414/*.java
4.	Run the rmi: 
    rmiregistry 30155
5.	In new cmd window, connect to the server:
    java -Djava.security.policy=security.policy ct414/ExamEngine 30155
6.	In third cmd window, run the client:
    java -Djava.security.policy=security.policy ct414/ClientLogin 30155





Screenshots of our Assessment System:
Starts with a log in screen where it asks for a student ID and password

![Test Image 1](https://github.com/16373576/Distributed-Systems-rmi/blob/master/GUIScreenshots/Picture1.png)

If the password is given incorrectly then the error message “Invalid login” pops up as shown below.

![Test Image 2](https://github.com/16373576/Distributed-Systems-rmi/blob/master/GUIScreenshots/Picture2.png)

Once the user logs in successfully then they get a list of the available assessments

![Test Image 3](https://github.com/16373576/Distributed-Systems-rmi/blob/master/GUIScreenshots/Picture3.png)

The user can select from these available assessments using the combo box

![Test Image 4](https://github.com/16373576/Distributed-Systems-rmi/blob/master/GUIScreenshots/Picture4.png)

In the case of this example the student can chose either maths or history

![Test Image 5](https://github.com/16373576/Distributed-Systems-rmi/blob/master/GUIScreenshots/Picture5.png)

When the user clicks start assignment the selected assessment will start. We see that the maths assignment has 3 questions and can see its due date. 

![Test Image 6](https://github.com/16373576/Distributed-Systems-rmi/blob/master/GUIScreenshots/Picture6.png)

The current question number can be selected using a combo box

![Test Image 7](https://github.com/16373576/Distributed-Systems-rmi/blob/master/GUIScreenshots/Picture7.png)

When a question number is selected it will display the question and the user must select the answer from the second combo box. Once all three question are completed the user will select submit assessment

![Test Image 8](https://github.com/16373576/Distributed-Systems-rmi/blob/master/GUIScreenshots/Picture8.png)

When submit assessment is selected a confirmation message that it was submitted will be shown and once ok is clicked the system will return to the list of assignments.

![Test Image 9](https://github.com/16373576/Distributed-Systems-rmi/blob/master/GUIScreenshots/Picture9.png)

If a student has no assignments the  following window is shown.

![Test Image 10](https://github.com/16373576/Distributed-Systems-rmi/blob/master/GUIScreenshots/Picture10.png)

If the session times out and the token goes invalid, then the user is returned to the login screen and told their session has expired.

![Test Image 11](https://github.com/16373576/Distributed-Systems-rmi/blob/master/GUIScreenshots/Picture11.png)
