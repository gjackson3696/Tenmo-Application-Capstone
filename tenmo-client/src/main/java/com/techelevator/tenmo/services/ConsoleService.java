package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;


import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);



    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void viewCurrentBalance(BigDecimal balance) {
        System.out.println(String.format("Your current account balance is: $%.2f",balance.doubleValue()));
    }

    public Transfer sendMoney(User currentUser, List<User> userList) {
        printSeparator();
        System.out.println("Users");
        System.out.println(String.format("%-10s%s","ID","Name"));
        printSeparator();

        userList.remove(currentUser);
        printUserList(userList);
        printSeparator();

        Transfer transfer = new Transfer();
        transfer.setUserToID(promptForInt("Enter ID of user you are sending to (0 to cancel):"));
        transfer.setAmount(promptForBigDecimal("Enter amount:"));
        transfer.setUserFromID(currentUser.getId().intValue());
        transfer.setTransferType("Send");
        transfer.setTransferTypeID(2);
        transfer.setTransferStatus("Approved");
        transfer.setTransferStatusID(2);

        return transfer;
    }

    private void printUserList(List<User> userList) {
        for(User user : userList) {
            System.out.println(String.format("%-10d%s",user.getId(),user.getUsername()));
        }
    }

    private void printSeparator() {
        System.out.println("-------------------------------------------");
    }

}
