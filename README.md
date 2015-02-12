# JobTest
This project was created as a test task for application at Sr. Java Developer role

The implementation

Testing: 
	mvn clean test  - will run Unit tests
	mvn clean integration-test - will run integration tests

Build:
	mvn clean install

Run from project root dir: 
	java -jar rest/target/rest-1.0-SNAPSHOT.jar 
or from rest project dir:
	mvn spring-boot:run

Calls:
	POST http://localhost:19080/login		- requires json data for login, returns session id in the request body.
	POST http://localhost:19080/logout		- requires sessionid property in the request header, returns "Bye." in the request body.
	POST http://localhost:19080/register

	GET http://localhost:19080/users/list		- requires sessionid property in the request header, returns json list of all users
	GET http://localhost:19080/users/favorites	- requires sessionid property in the request header, returns json list of favorites
	PUT http://localhost:19080/users/favorites	- requires sessionid property in the request header, returns json of added favorite user

	POST  http://localhost:19080/messages/send	- requires json message data and sessionid property in the request header, returns json of sent message
	GET  http://localhost:19080/messages/sent	- requires sessionid property in the request header, returns json list of sent messages
	GET  http://localhost:19080/messages/received	- requires sessionid property in the request header, returns json list of received messages

Data exaples:
	Login data: {"email":"john@doe.com", "password":"querty"}
	register data:{"email":"john@doe.com", "name":"John", "lastName":"Doe", "password":"querty"}
	send message: {	"recipientEmail":"john@doe.com", "subject":"subj1", "text": "text1"}
