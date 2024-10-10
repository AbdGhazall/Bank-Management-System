package BMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    //java.sql.SQLException: No database selected
    private static final String url = "jdbc:mysql://localhost:3306/banking_system"; //Ensure that the database name is included in the connection URL.
    private static final String username = "root";
    private static final String password = "123456";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            // Establish connection with the database using the provided URL, username, and password
            Connection connection = DriverManager.getConnection(url, username, password);

            // Initialize a Scanner object to take user input
            Scanner scanner = new Scanner(System.in);

            // Initialize the User, Accounts, and AccountManager objects, passing the database connection and scanner
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email; // Variable to store user email
            long account_number; // Variable to store account number

            // Infinite loop to continuously prompt user for actions until they exit
            while (true) {
                // Display the main menu for the banking system
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                // Capture the user’s main menu choice
                int choice1 = scanner.nextInt(); //for first switch

                // Switch statement to handle user choices for registration, login, or exit
                switch (choice1) {
                    case 1:
                        // User registration option
                        user.register();
                        break;

                    case 2:
                        // User login option
                        email = user.login(); // Capture the returned email after login for using it in account_exist METHOD
                        if (email != null) {
                            // Successful login
                            System.out.println();
                            System.out.println("User Logged In!");

                            // Check if the user already has a bank account
                            if (!accounts.account_exist(email)) {
                                // If the user does not have a bank account, prompt to create a new one
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");

                                // If the user chooses to open a new account, proceed to account creation
                                if (scanner.nextInt() == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                } else {
                                    break;
                                }
                            }

                            // Retrieve the user's account number
                            account_number = accounts.getAccount_number(email);

                            // Secondary menu for account operations
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter your choice: ");

                                // Capture the user’s account operation choice
                                choice2 = scanner.nextInt();

                                // Handle user choices for account operations
                                switch (choice2) {
                                    case 1:
                                        // Debit money from the user's account
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        // Credit money to the user's account
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        // Transfer money to another account
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        // Check the user's account balance
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        // Log out and return to the main menu
                                        break;
                                    default:
                                        // Invalid choice handler
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }
                        } else {
                            // Invalid login attempt
                            System.out.println("Incorrect Email or Password!");
                        }
                        break;

                    case 3:
                        // Exit the banking system
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        return; // Exit the loop and terminate the program

                    default:
                        // Handle invalid menu choices
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        } catch (SQLException e) {
            // Catch and print SQL exceptions for debugging
            e.printStackTrace();
        }

    }
}
