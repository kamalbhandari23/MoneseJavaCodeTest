# Monese Core Banking App
Monese java developer coding test.

According to the requirement document this application:
* Exposes REST API for:
  - Transferring funds between two accounts
  - Requesting account statement with account balance and list of transactions
* Accounts and transactions are persisted in a relational database

Technologies used :
* Spring Boot
* Spring Data JPA
* H2 Database
* Java 8
### Prerequisites for running the application
* git client 
* maven 
* Java 8 

### To build and run the application
```
git clone https://github.com/kamalbhandari23/MoneseJavaCodeTest.git
```
```
mvn clean install
```
```
java -jar target/corebanking-app-0.0.1-SNAPSHOT.jar
```
### Using the application
 Once the application is up and running, user can interact via  localhost:8080. URL are mentioned below
```
GET   localhost:8080/api/account/{accountId}/statement        Reports statement with transactions
POST  localhost:8080/api/account/transfer                     Transfer funds between accounts
```

###### Transfering funds between accounts
When transfering funds between accounts, the API is expecting a structured JSON message in the payload with values amount, fromAccountId,toAccountId. Like
```
{"amount":2000,"fromAccountId":10,"toAccountId":20}
```

###### In memory Database
Tables ACCOUNT and TRANSACTION are  stored in an in-memory database (H2 Database). During application startup two accounts are created with following account id
1) ACCOUNT_ID- 10
2) ACCOUNT_ID- 20

*Note - This application only provides functionality based on the requirements received. 
