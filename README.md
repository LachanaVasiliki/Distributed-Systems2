# Distributed-Systems2


PROBLEM DESCRIPTION
The distributed system we want to develop follows the client-server-server model and concerns a simplified view of a system for booking airline tickets. The system consists of two servers and many customers who represent the potential buyers of airline tickets.

More specifically, customers will submit their request through a graphical interface (GUI) to server 1. Server 1 is responsible for accepting all requests from the prospective customers, and after first contacting server 2, who is a base with all the information about the available flights, to inform them about the availability of flights. In case an attempt is made to reserve tickets from some client, server 1 is responsible for informing the client whether the reservation could be made or not after exchanging relevant messages with the server 2. The technologies that will be used to create the system are Java RMI for connecting clients to server 1 and sockets or datagrams for the connection of server 1 with server 2.

Customers
Customers should be able to search for available flights. For search will be able to set the following information:
From: Samos
To Athens
Number of Passengers: 2
Departure day: May 2
Return day: May 9

After customers enter the travel information they want to search for, they will be informed by server 1 if there are flights available or not. In case they exist, they will be displayed to the user and he will be given the option to select the flight you wish to book or search again. For herself day of departure or return, there may be more than one flight for him same destination, but at different times. In case the user chooses to do reservation, it should wait for server 1 to inform it if it finally did or not the reservation. In case there are no flights available or the reservation is not made the user will be given the option to search again.

1st server
Server 1 will be able to serve more than one client at the same time. Is responsible for receiving inquiries about all available itineraries from customers, and after first contacting server 2, to inform them about the available ones flights. In addition, he is responsible for accepting booking requests from customers, yes decide whether the reservation can be made or which reservation will be made in cooperation with server 2, and inform the customer of the progress of their reservation.

2nd server
Server 2 is a database with all information about available flights. Some of the information it will have for each flight is: Date, Time, From, To, Flight Number, Number of Seats, Ticket Price Server 2 after receiving a query from server 1 will search at basis and will reply to him with the available results. He is also responsible to informs its base in case there is a ticket reservation. The base with the information could be: SQLite database or other equivalent database.

