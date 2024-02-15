package com.smartbank.interfaces;

import com.smartbank.exceptions.InsufficientBalanceException;
import com.smartbank.exceptions.InvalidAmountException;
import com.smartbank.model.Account;

public interface Transferable {
    void transfer(Account receiver, double amount)
            throws InvalidAmountException, InsufficientBalanceException;
}
