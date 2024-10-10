# Banking Management System

This Banking System is a console-based Java application that simulates common banking operations. It allows users to register, log in, create accounts, check balances, credit and debit money, and transfer funds securely. The application uses a MySQL database for data storage and manipulation and implements JDBC to interact with the database. The main objective of this project is to simulate basic functionalities of a banking system with account and user management, ensuring a secure process through authentication and balance checks.

## Project Structure

-   `User.java` – Handles user registration and login.
-   `Accounts.java` – Manages account-related functions.
-   `AccountManager.java` – Provides functionalities like debit, credit, transfer, and balance checks.
-   `BankingApp.java` – Main application logic and menu.


## Features

-   **User Registration and Login**: Secure user registration and login using email and password.
-   **Account Management**: Users can create a bank account if they don't have one.
-   **Money Transactions**:
    -   **Debit Money**: Withdraw money from an account.
    -   **Credit Money**: Deposit money into an account.
    -   **Transfer Money**: Transfer money between accounts securely.
-   **Balance Check**: Check the current balance of an account.
-   **Security**: All transactions require a security pin to ensure safe operations.
-   **Database Persistence**: The application uses MySQL to persist data between sessions.



## Topics Used

-   **Object-Oriented Programming (OOP)**: Encapsulation, Inheritance, and Polymorphism.
-   **Java JDBC**: Connecting Java applications with MySQL for database operations.
-   **SQL**: Database queries to handle account and transaction operations.
-   **Exception Handling**: Safely handling SQL and input errors.
-   **Input Validation**: Checking user input to ensure secure and valid operations.


## Technologies Used

-   **Java SE**
-   **MySQL**
-   **JDBC (Java Database Connectivity)**
-   **IntelliJ IDEA**

## How to Run the Project



1.  **Clone the Project:**
    
    ```bash
    git clone https://github.com/AbdGhazall/Bank-Management-System.git`
    ``` 
    
2.  **Set Up Database**:
    
    -   Create a MySQL database for the project.
    -   Run the following SQL script to create the necessary tables:

    
    ```sql
    CREATE DATABASE banking_system;
    
    USE banking_system;
    
	CREATE TABLE accounts ( 
	account_number BIGINT NOT NULL PRIMARY KEY, 
	full_name VARCHAR(255) NOT NULL, 
	email VARCHAR(255) NOT NULL UNIQUE, 
	balance DECIMAL(10, 2), 
	security_pin CHAR(4) NOT NULL 
	);
	
    CREATE TABLE user (
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL PRIMARY KEY,
    password VARCHAR(255) NOT NULL
	);
    ```
3.  **Configure Database Connection**:
    
    -   Update the connection details in the `BankingApp.java` file:
    
    ```java
    String url = "jdbc:mysql://localhost:3306/banking_system";
    String username = "your_mysql_username";
    String password = "your_mysql_password";` 
    ```
    

 4. **Java Setup**:
    -   Install and configure  [MySQL JDBC driver](https://dev.mysql.com/downloads/connector/j/).
    -   Compile and run the  `BankingApp.java`  file.



## Screenshots



- **Output**
	- 

- **Database Tables**  
	- user table
 	  ![users](https://github.com/user-attachments/assets/8b4f202d-844a-4b59-bb6c-350dc5a1d1ab)
