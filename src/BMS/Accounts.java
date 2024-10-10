package BMS;

import java.sql.Connection;
import java.util.Scanner;
import java.sql.*;

public class Accounts {
    // Fields for database connection and input
    private Connection connection;
    private Scanner scanner;

    // Constructor to initialize connection and scanner
    public Accounts(Connection connection, Scanner scanner) {
        this.scanner = scanner;
        this.connection = connection;
    }

    // Method to open a new account
    public long open_account(String email) {
        // Check if an account already exists for this email
        if (!account_exist(email)) { // If account doesn't exist
            String open_account_query = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";

            // Collect account details from the user
            scanner.nextLine();
            System.out.print("Enter Full Name: ");
            String full_name = scanner.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = scanner.nextDouble(); // Balance input
            scanner.nextLine(); // Clear buffer
            System.out.print("Enter Security Pin: ");
            String security_pin = scanner.nextLine(); // Security pin input

            try {
                // Generate a new account number by calling generateAccountNumber()
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1, account_number); // Account number
                preparedStatement.setString(2, full_name); // Full name
                preparedStatement.setString(3, email); // Email
                preparedStatement.setDouble(4, balance); // Initial balance
                preparedStatement.setString(5, security_pin); // Security pin

                // Execute the insert query
                int rowsAffected = preparedStatement.executeUpdate(); // Since INSERT, use executeUpdate()

                // Return the account number if rows affected
                if (rowsAffected > 0) {
                    return account_number;
                } else {
                    throw new RuntimeException("Account Creation failed!!");
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle SQL exceptions
            }
        }
        // Throw an error if account already exists
        throw new RuntimeException("Account Already Exists");
    }

    // Method to check if an account exists for a specific email
    public boolean account_exist(String email) {
        String query = "SELECT account_number from Accounts WHERE email = ?"; // SQL query to check account existence

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email); // Set email parameter
            ResultSet resultSet = preparedStatement.executeQuery(); // Execute the query

            // If account exists, return true
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        return false; // Return false if no account found or exception occurs
    }

    // Private method to generate a unique account number
    private long generateAccountNumber() {
        try {
            // Query to get the last account number in descending order
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number from Accounts ORDER BY account_number DESC LIMIT 1");

            // If an account number exists, increment it by 1 for the new account
            if (resultSet.next()) {
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number + 1;
            } else {
                // If no account exists, return a default starting account number
                return 10000100;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        // Return default account number in case of failure
        return 10000100;
    }

    // Method to retrieve the account number using the email
    public long getAccount_number(String email) {
        String query = "SELECT account_number from Accounts WHERE email = ?"; // SQL query to get account number

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email); // Set email parameter
            ResultSet resultSet = preparedStatement.executeQuery(); // Execute query

            // If account exists, return the account number
            if (resultSet.next()) {
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        // Throw an exception if no account found
        throw new RuntimeException("Account Number Doesn't Exist!");
    }
}
