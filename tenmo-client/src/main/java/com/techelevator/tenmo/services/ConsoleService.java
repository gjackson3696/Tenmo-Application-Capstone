package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

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

    public void displayTransactionHistory(Map<Integer, Transfer> transfers, User currentUser, boolean isPending) {
        printSeparator();
        System.out.println("Transfers");
        System.out.println(String.format("%-10s%-25s%s", "ID", "From/To", "Amount"));
        printSeparator();
        Set<Integer> keys = transfers.keySet();
        for (int key : keys) {
            Transfer transfer = transfers.get(key);
            if(isPending) {
                if(transfer.getTransferStatus().equals("Pending")) {
                    printTransfer(transfer,currentUser,key,isPending);
                }
            } else {
                printTransfer(transfer, currentUser, key, isPending);
            }
        }
        printSeparator();
    }

    public void displayTransactionDetails(Map<Integer, Transfer> transfers) {
        int response = promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        getId:
        while (true) {
            if (transfers.containsKey(response)) {
                Transfer transfer = transfers.get(response);
                printSeparator();
                System.out.println("Transfer Details");
                printSeparator();
                System.out.println("Id: " + transfer.getTransferID());
                System.out.println("From: " + transfer.getUsernameFrom());
                System.out.println("To: " + transfer.getUsernameTo());
                System.out.println("Type: " + transfer.getTransferType());
                System.out.println("Status: " + transfer.getTransferStatus());
                System.out.println("Amount: " + String.format("$%.2f", transfer.getAmount().doubleValue()));
                break getId;
            } else if (response == 0) {
                break getId;
            }
            response = promptForInt("Please enter a valid transfer ID (0 to cancel): ");
        }

    }

    public int pendingRequests() {
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        printSeparator();
        return promptForInt("Please choose an option:");
    }

    private void printTransfer(Transfer transfer, User currentUser, int key, boolean isPending) {
        if(!isPending) {
            if (transfer.getTransferType().equals("Send") || transfer.getTransferStatus().equals("Pending")) {
                if (transfer.getUsernameTo().equals(currentUser.getUsername())) {
                    System.out.println(String.format("%-10d%-25s$%.2f", key, "From: " + transfer.getUsernameFrom(), transfer.getAmount()));
                } else {
                    System.out.println(String.format("%-10d%-25s$%.2f", key, "To: " + transfer.getUsernameTo(), transfer.getAmount()));
                }
            } else {
                if (transfer.getUsernameTo().equals(currentUser.getUsername())) {
                    System.out.println(String.format("%-10d%-25s$%.2f", key, "From: " + transfer.getUsernameFrom(), transfer.getAmount()));
                } else {
                    System.out.println(String.format("%-10d%-25s$%.2f", key, "To: " + transfer.getUsernameTo(), transfer.getAmount()));
                }
            }
        } else {
            if(transfer.getTransferStatus().equals("Pending")) {
                if (transfer.getUsernameTo().equals(currentUser.getUsername())) {
                    System.out.println(String.format("%-10d%-25s$%.2f", key, "From: " + transfer.getUsernameFrom(), transfer.getAmount()));
                } else {
                    System.out.println(String.format("%-10d%-25s$%.2f", key, "To: " + transfer.getUsernameTo(), transfer.getAmount()));
                }
            }
        }
    }

    public Transfer sendMoney(User currentUser, List<User> userList) {
        listUsers(currentUser, userList);

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

    public Transfer requestMoney(User currentUser, List<User> userList) {
       listUsers(currentUser, userList);

        Transfer transfer = new Transfer();
        transfer.setUserFromID(promptForInt("Enter ID of user you are requesting from (0 to cancel):"));
        transfer.setAmount(promptForBigDecimal("Enter amount:"));
        transfer.setUserToID(currentUser.getId().intValue());
        transfer.setTransferType("Request");
        transfer.setTransferTypeID(1);
        transfer.setTransferStatus("Pending");
        transfer.setTransferStatusID(1);

        return transfer;
    }
    public void unableToApprove() {
        System.out.println("You are unable to approve this transaction.");
    }
    public void invalidUser() {
        System.out.println("Please enter a valid user ID.");
    }

    public void invalidTransaction() {
        System.out.println("This transaction could not be processed.");
    }

    public void invalidChoice() {
        System.out.println("Please enter a valid option.");
    }

    private void listUsers(User currentUser, List<User> userList) {
        printSeparator();
        System.out.println("Users");
        System.out.println(String.format("%-10s%s","ID","Name"));
        printSeparator();

        userList.remove(currentUser);
        printUserList(userList);
        printSeparator();
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
