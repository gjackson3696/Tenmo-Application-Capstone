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
        Map<Integer,Transfer> transfers = new HashMap<>();
        String sql = "SELECT transfer.*, transfer_type.transfer_type_desc, transfer_status.transfer_status_desc " +
                "FROM transfer" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id" +
                "WHERE transfer.account_from = ? OR transfer.account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountID,accountID);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.put(transfer.getTransferID(), transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransfer(int transferID, int accountID) {
        Transfer transfer = null;
        String sql = "SELECT transfer.*, transfer_type.transfer_type_desc, transfer_status.transfer_status_desc, " +
                "(SELECT user_id FROM account JOIN transfer ON account.account_id = transfer.account_to) AS user_to, " +
                "(SELECT user_id FROM account JOIN transfer ON account.account_id = transfer.account_from) AS user_from " +
                "FROM transfer" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id" +
                "WHERE transfer.transfer_id = ? AND (transfer.account_from = ? OR transfer.account_to = ?);";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferID, accountID, accountID);
        transfer = mapRowToTransfer(result);

        return transfer;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer(amount, account_from, account_to, transfer_status_id, transfer_type_id)" +
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
        transfer.setUserFromID(results.getInt("user_from"));
        transfer.setUserToID(results.getInt("user_to"));
        return transfer;
    }
}
