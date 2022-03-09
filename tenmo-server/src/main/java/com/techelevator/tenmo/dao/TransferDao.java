package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.List;

public interface TransferDao {
    List<Transfer> listAll(User user);
    Transfer getTransfer(int transferID);
    Transfer createTransfer(Transfer transfer);
    void updateTransfer(Transfer transfer);
}
