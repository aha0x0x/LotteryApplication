# LotteryApplication

a fun project to mimic office lottery. 

Compiling and bulding Instructions
----------------------------------
This project uses maven to build the project. It will automatically download required depandancies so internet connectivity is required.

* To compile
`mvn compile `

* To run unit tests
`mvn test`

* To build/package:
`mvn package`

* after bulding, to execute 
`mvn exec:java -Dexec.mainClass="code.aha.lottery.Lottery"`


Playing Intructions
-------------------

application supports following list of commands:

* `quit` quit the application
* `purchase <username>` purchase a ticket. Note that username is required otherwise no purchase will be done. eg. `purchase Joe`
* `winners` prints the winners of the current draw. 
* `draw` starts a new draw. All purchased tickets are discarded and purchase list reset. 
