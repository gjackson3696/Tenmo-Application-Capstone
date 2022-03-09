package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

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
}
