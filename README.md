# Bank-System
###Operating System Project（2016）

this project is to write a Multi-threaded TCP Server Application which allows multiple customers to update their bank accounts.

###Requirements:

1. Register with the system

2. Log-in to the banking system from the client application to the server application.

3. Change customer details.

4. Make Lodgements to their Bank Account.

5. Make Withdrawal from their Bank Account (Note: Each User has a credit limit of €1000).

6. View the last ten transactions on their bank account.

7. The server application should not provide any service to a client application that can complete the authentication.

8. The server should hold a list of valid users of the service and a list of all the users previous transactions.

9. When the user logs in they should receive their current balance.

###Overview

a). client and server connect by using socket.

b). user's info would write into a file after register/operations(logement/Withdrawal etc.)

c). After connection, the first manu:

1. Login

2. Register

3. Exit

d). if user logged in, the scend manu will come out:

=> l    Lodgement

=> w    Withdrawal

=> h    Show History

=> c    Change Password

=> e    logout

e). Also provids some errors handling. e.g. input some char which doesn't list on the manu and check username&password etc.

###How to run it

1. before run it, make sure the jdk/jre environment installed correctly.

2. use two different version Eclipses run client.java & server.java respectivily.In order to connect, the server's ip should be known in advance.

3. then, operate follow the tips.


