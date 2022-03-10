package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.List;
import java.util.Map;

public interface TransferDao {
    Map<Integer,Transfer> listAll(int accountID);
    //Transfer getTransfer(int transferID, int accountID);
    Transfer createTransfer(Transfer transfer);
    void updateTransferStatus(Transfer transfer);
}
