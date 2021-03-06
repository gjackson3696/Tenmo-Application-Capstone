package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    @Override
    public Map<Integer,Transfer> listAll(int accountID) {
        Map<Integer,User> accountUsers = getAccountUsers();
        Map<Integer,Transfer> transfers = new HashMap<>();
        String sql = "SELECT transfer.*, transfer_type.transfer_type_desc, transfer_status.transfer_status_desc " +
                "FROM transfer " +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id " +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "WHERE transfer.account_from = ? OR transfer.account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountID,accountID);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfer.setUserFromID(accountUsers.get(transfer.getAccountFrom()).getId().intValue());
            transfer.setUsernameFrom(accountUsers.get(transfer.getAccountFrom()).getUsername());
            transfer.setUserToID(accountUsers.get(transfer.getAccountTo()).getId().intValue());
            transfer.setUsernameTo(accountUsers.get(transfer.getAccountTo()).getUsername());
            transfers.put(transfer.getTransferID(), transfer);
        }
        return transfers;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer(amount, account_from, account_to, transfer_status_id, transfer_type_id) " +
                "VALUES(?, ?, ?, ?, ?) RETURNING transfer_id;";
        int transferID = jdbcTemplate.queryForObject(sql, int.class, transfer.getAmount(), transfer.getAccountFrom(),
                transfer.getAccountTo(), transfer.getTransferStatusID(), transfer.getTransferTypeID());
        transfer.setTransferID(transferID);
        return transfer;
    }

    @Override
    public void updateTransferStatus(Transfer transfer) {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, transfer.getTransferStatusID(), transfer.getTransferID());
    }

    private Map<Integer,User> getAccountUsers() {
        Map<Integer,User> accountUsernames = new HashMap<>();
        String sql = "SELECT u.username, u.user_id, acc.account_id " +
                "FROM tenmo_user u " +
                "JOIN account acc ON u.user_id = acc.user_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = new User();
            user.setId(results.getLong("user_id"));
            user.setUsername(results.getString("username"));
            accountUsernames.put(results.getInt("account_id"), user);
        }
        return accountUsernames;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferID(results.getInt("transfer_id"));
        transfer.setAmount(results.getBigDecimal("amount"));
        transfer.setAccountFrom(results.getInt("account_from"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setTransferStatus(results.getString("transfer_status_desc"));
        transfer.setTransferType(results.getString("transfer_type_desc"));
        transfer.setTransferStatusID(results.getInt("transfer_status_id"));
        transfer.setTransferTypeID(results.getInt("transfer_type_id"));
        return transfer;
    }
}
