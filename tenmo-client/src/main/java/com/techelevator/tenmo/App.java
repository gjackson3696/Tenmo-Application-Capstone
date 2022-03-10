package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;

import java.util.List;
import java.util.Map;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();
    private final UserService userService = new UserService();
    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            String token = currentUser.getToken();
            accountService.setAuthToken(token);
            transferService.setAuthToken(token);
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        consoleService.viewCurrentBalance(accountService.getBalance());
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
        Map<Integer, Transfer> allTransfers = transferService.getAllTransfers();
        consoleService.displayTransactionHistory(allTransfers, currentUser.getUser(), false);
        consoleService.displayTransactionDetails(allTransfers);
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
        Map<Integer, Transfer> allTransfers = transferService.getAllTransfers();
        consoleService.displayTransactionHistory(allTransfers, currentUser.getUser(), true);
        int response = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
        if (isValidTransfer(response, allTransfers)) {
            Transfer transfer = allTransfers.get(response);
            int choice = consoleService.pendingRequests();
            while (choice < 0 || choice > 2) {
                consoleService.invalidChoice();
                choice = consoleService.pendingRequests();
            }
            if (choice == 1) {
                if (accountService.getBalance().compareTo(transfer.getAmount()) >= 0 && isAbleToApprove(transfer, currentUser.getUser())) {
                    transfer.setTransferStatus("Approved");
                    transfer.setTransferStatusID(2);
                    transferService.updateTransferStatus(transfer);
                    transferService.sendMoney(transfer);
                } else if (!isAbleToApprove(transfer, currentUser.getUser())){
                    consoleService.unableToApprove();
                } else {
                    consoleService.invalidTransaction();
                }
            }
            if (choice == 2) {
                transfer.setTransferStatus("Rejected");
                transfer.setTransferStatusID(3);
                transferService.updateTransferStatus(transfer);
            }
        } else if (response != 0) {
            consoleService.invalidUser();
        }
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
		Transfer newTransfer = consoleService.sendMoney(currentUser.getUser(),userService.getUserList());
        if (accountService.getBalance().compareTo(newTransfer.getAmount()) >= 0 && isValidUser(newTransfer.getUserToID())) {
            transferService.sendMoney(newTransfer);
            transferService.addTransfer(newTransfer);
        } else {
            consoleService.invalidTransaction();
        }

    }

	private void requestBucks() {
		// TODO Auto-generated method stub
		Transfer newTransfer = consoleService.requestMoney(currentUser.getUser(),userService.getUserList());
        if(isValidUser(newTransfer.getUserFromID())) {
            transferService.addTransfer(newTransfer);
        } else {
            consoleService.invalidTransaction();
        }
	}

    private boolean isValidUser(int userID) {
        List<User> userList = userService.getUserList();
        boolean found = false;
        for(User user : userList) {
            if (user.getId() == userID) {
                found = true;
            }
        }
        return found;
    }

    private boolean isValidTransfer(int transactionID, Map<Integer, Transfer> transfers) {
        if (transfers.containsKey(transactionID)) {
            return true;
        }
        return false;
    }

    private boolean isAbleToApprove(Transfer transfer, User currentUser) {
        if (currentUser.getId() == transfer.getUserFromID()) {
            return true;
        }
        return false;
    }
}
