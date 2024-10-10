package BMS;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;
import java.sql.Connection;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    //--------------------------------------------------------------------------------------------------
    public void credit_money(long account_number) throws SQLException {
        // Clear the input buffer (in case there are leftover newlines from previous inputs)
        scanner.nextLine();

        // Prompt the user to enter the amount to be credited
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble(); // Capture the amount input
        scanner.nextLine(); // Clear the buffer again after capturing a double

        // Prompt the user to enter their security pin
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine(); // Capture the security pin input

        try {
            // Disable auto-commit mode to allow for manual transaction management
            connection.setAutoCommit(false);

            // Check if the account number provided is valid (non-zero)
            if (account_number != 0) {
                // Prepare SQL statement to select the account with the given account number and security pin
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM Accounts WHERE account_number = ? and security_pin = ?"
                );
                // Set the parameters (account number and security pin) for the query
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);

                // Execute the query and store the result in a ResultSet object
                ResultSet resultSet = preparedStatement.executeQuery();

                // If the account with the provided account number and pin exists
                if (resultSet.next()) {
                    // Prepare the SQL query to update the balance by adding the amount
                    String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);

                    // Set the parameters (amount to be credited and account number) for the update query
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_number);

                    // Execute the update query and get the number of affected rows
                    int rowsAffected = preparedStatement1.executeUpdate();

                    // If the update was successful (rows affected > 0), commit the transaction
                    if (rowsAffected > 0) {
                        // Confirm the amount was successfully credited
                        System.out.println(amount + " JD." + " credited Successfully");

                        // Commit the transaction to make changes permanent
                        connection.commit();

                        // Re-enable auto-commit mode
                        connection.setAutoCommit(true);
                        return; // Exit the method after successful transaction
                    } else {
                        // If no rows were affected, transaction failed
                        System.out.println("Transaction Failed!");

                        // Roll back the transaction to undo any changes made
                        connection.rollback();

                        // Re-enable auto-commit mode
                        connection.setAutoCommit(true);
                    }
                } else {
                    // If no matching account was found, inform the user about the invalid security pin
                    System.out.println("Invalid Security Pin!");
                }
            }
        } catch (SQLException e) {
            // Print the SQL exception stack trace if any errors occur
            e.printStackTrace();
        }

        // Ensure auto-commit is re-enabled even in case of exceptions or errors
        connection.setAutoCommit(true);
    }

    //----------------------------------------------------------------------------------------------------
    public void debit_money(long account_number) throws SQLException {
        // Clear the input buffer to ensure clean inputs
        scanner.nextLine();

        // Prompt the user to enter the amount to be debited
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble(); // Capture the amount input
        scanner.nextLine(); // Clear the buffer after capturing the double value

        // Prompt the user to enter their security pin
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine(); // Capture the security pin

        try {
            // Disable auto-commit for manual transaction management
            connection.setAutoCommit(false);

            // Ensure the account number is valid (non-zero)
            if (account_number != 0) {
                // Prepare a SQL query to validate the account number and security pin
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM Accounts WHERE account_number = ? and security_pin = ?"
                );
                // Set the parameters for account number and security pin in the SQL query
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);

                // Execute the query and store the result
                ResultSet resultSet = preparedStatement.executeQuery();

                // If the account and security pin match, proceed with balance check
                if (resultSet.next()) {
                    // Get the current balance of the account
                    double current_balance = resultSet.getDouble("balance");

                    // Check if there is sufficient balance to debit the requested amount
                    if (amount <= current_balance) {
                        // Prepare a SQL query to debit the specified amount from the account
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);

                        // Set the parameters for the amount and account number in the update query
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_number);

                        // Execute the update query to debit the balance
                        int rowsAffected = preparedStatement1.executeUpdate();

                        // If the update was successful, commit the transaction
                        if (rowsAffected > 0) {
                            System.out.println(amount + " JD. " + " debited Successfully");

                            // Commit the transaction to make changes permanent
                            connection.commit();

                            // Re-enable auto-commit mode
                            connection.setAutoCommit(true);
                            return; // Exit the method after successful transaction
                        } else {
                            // If no rows were affected, transaction failed
                            System.out.println("Transaction Failed!");

                            // Rollback the transaction to undo changes
                            connection.rollback();

                            // Re-enable auto-commit mode
                            connection.setAutoCommit(true);
                        }
                    } else {
                        // If there is not enough balance, notify the user
                        System.out.println("Insufficient Balance!");
                    }
                } else {
                    // If no matching account is found, notify the user about invalid security pin
                    System.out.println("Invalid Pin!");
                }
            }
        } catch (SQLException e) {
            // Print the SQL exception stack trace for debugging in case of errors
            e.printStackTrace();
        }

        // Ensure auto-commit is re-enabled after the transaction, even if exceptions occur
        connection.setAutoCommit(true);
    }

    //----------------------------------------------------------------------------------------------------
    public void transfer_money(long sender_account_number) throws SQLException {
        // Clear the input buffer to ensure clean inputs
        scanner.nextLine();

        // Prompt the user to enter the receiver's account number
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong(); // Capture the receiver's account number

        // Prompt the user to enter the transfer amount
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble(); // Capture the transfer amount
        scanner.nextLine(); // Clear the buffer

        // Prompt the user to enter their security pin
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine(); // Capture the security pin

        try {
            // Disable auto-commit for manual transaction management
            connection.setAutoCommit(false);

            // Ensure both sender and receiver account numbers are valid (non-zero)
            if (sender_account_number != 0 && receiver_account_number != 0) {
                // Prepare a SQL query to validate the sender's account and security pin
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?"
                );
                // Set the sender's account number and security pin for validation
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);

                // Execute the query to check the sender's credentials
                ResultSet resultSet = preparedStatement.executeQuery();

                // If the account and security pin match, proceed with the transfer
                if (resultSet.next()) {
                    // Get the sender's current balance
                    double current_balance = resultSet.getDouble("balance");

                    // Check if the sender has enough balance to transfer the requested amount
                    if (amount <= current_balance) {
                        // Prepare SQL queries for debiting the sender's account and crediting the receiver's account
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

                        // Create prepared statements for the debit and credit operations
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);

                        // Set the parameters for debiting the sender's account
                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, sender_account_number);

                        // Set the parameters for crediting the receiver's account
                        creditPreparedStatement.setDouble(1, amount);
                        creditPreparedStatement.setLong(2, receiver_account_number);

                        // Execute the debit and credit operations
                        int rowsAffected1 = debitPreparedStatement.executeUpdate(); // Debit sender
                        int rowsAffected2 = creditPreparedStatement.executeUpdate(); // Credit receiver

                        // If both debit and credit were successful, commit the transaction
                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("Transaction Successful!");
                            System.out.println(amount + " JD." + " Transferred Successfully");

                            // Commit the transaction to finalize the transfer
                            connection.commit();

                            // Re-enable auto-commit mode
                            connection.setAutoCommit(true);
                            return; // Exit the method after successful transaction
                        } else {
                            // If any update failed, roll back the transaction
                            System.out.println("Transaction Failed");
                            connection.rollback();

                            // Re-enable auto-commit mode
                            connection.setAutoCommit(true);
                        }
                    } else {
                        // If insufficient balance, notify the user
                        System.out.println("Insufficient Balance!");
                    }
                } else {
                    // If the security pin is invalid, notify the user
                    System.out.println("Invalid Security Pin!");
                }
            } else {
                // If any of the account numbers is invalid (0), notify the user
                System.out.println("Invalid account number");
            }
        } catch (SQLException e) {
            // Print the SQL exception stack trace for debugging in case of errors
            e.printStackTrace();
        }

        // Ensure auto-commit is re-enabled after the transaction, even if exceptions occur
        connection.setAutoCommit(true);
    }

    //-------------------------------------------------------------------------------------------------------
    public void getBalance(long account_number) {
        // Clear the input buffer to ensure clean inputs
        scanner.nextLine();

        // Prompt the user to enter the security pin
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine(); // Capture the security pin

        try {
            // Prepare a SQL query to retrieve the account balance by validating the account number and security pin
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?"
            );
            // Set the account number and security pin for the prepared statement
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);

            // Execute the query and retrieve the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // If the query returns a result (i.e., account exists and security pin matches)
            if (resultSet.next()) {
                // Extract the balance from the result set
                double balance = resultSet.getDouble("balance");

                // Print the balance to the console
                System.out.println("Balance: " + balance);
            } else {
                // If no matching account is found or security pin is incorrect, print an error message
                System.out.println("Invalid Pin!");
            }
        } catch (SQLException e) {
            // Print the SQL exception stack trace for debugging in case of errors
            e.printStackTrace();
        }
    }


}
