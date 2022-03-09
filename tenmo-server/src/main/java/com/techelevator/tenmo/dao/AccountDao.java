package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
    BigDecimal getBalance(int userID);
    void deposit(Account account, BigDecimal amount);
    boolean withdraw(Account account, BigDecimal amount);
    Account getAccountByUserID(int userID);
    Account getAccountByAccountID(int accountID);
}
