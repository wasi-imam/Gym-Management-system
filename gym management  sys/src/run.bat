@echo off
cd /d "C:\Users\wasii\Desktop\gym management sys\src"
javac -cp ".;../lib/ojdbc11.jar" DBConnection.java MemberPanel.java PaymentPanel.java MainFrame.java
java -cp ".;../lib/ojdbc11.jar" MainFrame
pause