package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    @Override
    public List<Transfer> listAll(User user) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer.*, transfer_type.transfer_type_desc, transfer_status.transfer_status_desc " +
                "FROM transfer" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransfer(int transferID) {
        Transfer transfer = null;
        String sql = "SELECT transfer.*, transfer_type.transfer_type_desc, transfer_status.transfer_status_desc " +
                "FROM transfer" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id" +
                "WHERE transfer_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferID);
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
        return transfer;
    }
}
