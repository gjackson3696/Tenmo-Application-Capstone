package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private int accountID, userID;
    private String username;
    private BigDecimal balance;

    public Account(){}

    public int getAccountID() {
        return accountID;
    }

    public Account(int accountID, int userID, String username, BigDecimal balance) {
        this.accountID = accountID;
        this.userID = userID;
        this.username = username;
        this.balance = balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void deposit(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) == 1) { // If amount is greater than zero
            balance.add(amount);
        }
    }

    public boolean withdraw(BigDecimal amount) {
        if(amount.compareTo(balance) <= 0 && amount.compareTo(BigDecimal.ZERO) == 1) { // If amount is less than balance and greater than zero
            balance.subtract(amount);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Your current account balance is: $%.2f",balance.doubleValue());
    }
}
