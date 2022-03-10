package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Transfer {
    private int transferID,  transferTypeID, transferStatusID;
    @Min(value = 1, message = "Please enter a valid account number")
    private int accountFrom, accountTo;
    @NotEmpty
    private String transferType, transferStatus;
    @Positive
    private BigDecimal amount;

    public Transfer() {}

    public Transfer(int transferID, int accountFrom, int accountTo, int transferTypeID, int transferStatusID, String transferType, String transferStatus, BigDecimal amount) {
        this.transferID = transferID;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.transferTypeID = transferTypeID;
        this.transferStatusID = transferStatusID;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.amount = amount;
    }

    public int getTransferTypeID() {
        return transferTypeID;
    }

    public void setTransferTypeID(int transferTypeID) {
        this.transferTypeID = transferTypeID;
    }

    public int getTransferStatusID() {
        return transferStatusID;
    }

    public void setTransferStatusID(int transferStatusID) {
        this.transferStatusID = transferStatusID;
    }

    public int getTransferID() {
        return transferID;
    }

    public void setTransferID(int transferID) {
        this.transferID = transferID;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
