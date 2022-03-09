package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transferID, accountFrom, accountTo;
    private String transferType, transferStatus;
    private BigDecimal amount;

    public Transfer() {}

    public Transfer(int transferID, int accountFrom, int accountTo, String transferType, String transferStatus, BigDecimal amount) {
        this.transferID = transferID;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.amount = amount;
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
