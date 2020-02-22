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
    
