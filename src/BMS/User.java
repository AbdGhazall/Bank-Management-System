package BMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    // Fields to hold the database connection and scanner for user input
    private Connection connection;
    private Scanner scanner;

    // Constructor that initializes the connection and scanner
    public User(Connection connection, Scanner scanner) {
        this.connection = connection; // Assigns the passed Connection object to 'connection'
        this.scanner = scanner; // Assigns the passed Scanner object to 'scanner'
    }

    // Method for registering a new user
    public void register() {
        scanner.nextLine(); // Clear any leftover new lines from previous inputs
        System.out.print("Full Name: ");
        String full_name = scanner.nextLine(); // Capture the full name
        System.out.print("Email: ");
        String email = scanner.nextLine(); // Capture the email
        System.out.print("Password: ");
        String password = scanner.nextLine(); // Capture the password

        // Check if the user already exists by calling user_exist() method
        if (user_exist(email)) {
            System.out.println("User Already Exists for this Email Address!!");
            return; // If user exists, stop registration
        }

        // SQL query for inserting a new user into the User table
        String register_query = "INSERT INTO User(full_name, email, password) VALUES(?, ?, ?)"; // Insert new user

        try {
            // Prepare the SQL query for execution
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            // Set the full name, email, and password as parameters for the query
            preparedStatement.setString(1, full_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            // Execute the query and check how many rows are affected
            int affectedRows = preparedStatement.executeUpdate(); // Since INSERT, we use executeUpdate()

            // If rows are affected, registration was successful
            if (affectedRows > 0) {
                System.out.println("Registration Successful!");
            } else {
                System.out.println("Registration Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print error if any exception occurs
        }
    }

    // Method for logging in a user
    // return the email
    public String login() {
        scanner.nextLine(); // Clear input buffer
        System.out.print("Email: ");
        String email = scanner.nextLine(); // Get email input
        System.out.print("Password: ");
        String password = scanner.nextLine(); // Get password input

        // SQL query for selecting user based on provided email and password
        String login_query = "SELECT * FROM User WHERE email = ? AND password = ?"; // Verifying credentials

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            // Set email and password as parameters
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            // Execute the query and store the result in ResultSet
            ResultSet resultSet = preparedStatement.executeQuery(); // We use executeQuery() for SELECT queries

            // If a record exists, return the email (successful login)
            if (resultSet.next()) {
                return email; //for saving it in the switch
            } else {
                return null; // If no record found, return null (login failed)
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        return null;
    }

    // Method to check if a user exists based on the email
    public boolean user_exist(String email) {
        String query = "SELECT * FROM user WHERE email = ?"; // SQL query to check if user exists

        try {
            // Prepare the query and set the email parameter
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            // Execute the query and store the result in ResultSet
            ResultSet resultSet = preparedStatement.executeQuery(); // Again using executeQuery() for SELECT

            // If the result set has data, user exists
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        return false; // Return false if exception occurs or no user found
    }
}
