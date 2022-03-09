package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class TenmoController {
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TransferDao transferDao;
    @Autowired
    private UserDao userDao;

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal balance(Principal principal) {
        return accountDao.getBalance(userDao.findIdByUsername(principal.getName()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public void createTransfer(Transfer transfer) {
        transferDao.createTransfer(transfer);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/transaction", method = RequestMethod.POST)
    public void transaction(Transfer transfer) {
        Account fromAccount = accountDao.getAccountByAccountID(transfer.getAccountFrom());
        Account toAccount = accountDao.getAccountByAccountID(transfer.getAccountTo());
        if(accountDao.withdraw(fromAccount, transfer.getAmount())) {
            accountDao.deposit(toAccount, transfer.getAmount());
        }
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> listUsers() {
        return userDao.findAll();
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public Map<Integer,Transfer> listTransfers(Principal principal) {
        int accountID = accountDao.getAccountByUserID(userDao.findIdByUsername(principal.getName())).getAccountID();
        return transferDao.listAll(accountID);
    }
}
