package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.List;

public class JdbcTransferDao implements TransferDao{
    @Override
    public List<Transfer> listAll(User user) {
        return null;
    }

    @Override
    public Transfer getTransfer(int transferID) {
        return null;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        return null;
    }

    @Override
    public void updateTransfer(Transfer transfer) {

    }
}
