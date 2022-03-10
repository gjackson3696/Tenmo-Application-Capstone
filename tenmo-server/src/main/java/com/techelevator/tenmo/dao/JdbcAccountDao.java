package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    @Override
    public BigDecimal getBalance(int userID) {
        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql,BigDecimal.class,userID);
        return balance;
    }

    @Override
    public void deposit(Account account, BigDecimal amount) {
        account.deposit(amount);
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql,account.getBalance(),account.getAccountID());
    }

    @Override
    public boolean withdraw(Account account, BigDecimal amount) {
        if(account.withdraw(amount)) {
            String sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
            jdbcTemplate.update(sql,account.getBalance(),account.getAccountID());
            return true;
        }
        return false;
    }

    @Override
    public Account getAccountByUserID(int userID) {
        Account account = null;
        String sql = "SELECT account.*, username FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE account.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userID);
        account = mapRowToAccount(results);
        return account;
    }

    @Override
    public Account getAccountByAccountID(int accountID) {
        Account account = null;
        String sql = "SELECT account.*, username FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE account.account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID);
        account = mapRowToAccount(results);
        return account;
    }

    Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        if(results.next()) {
            account.setAccountID(results.getInt("account_id"));
            account.setUserID(results.getInt("user_id"));
            account.setUsername(results.getString("username"));
            account.setBalance(results.getBigDecimal("balance"));
        }
        return account;
    }
}
