package com.smartbank.interfaces;

import com.smartbank.exceptions.InsufficientBalanceException;
import com.smartbank.exceptions.InvalidAmountException;

public interface TransactionOperations {
    void deposit(double amount) throws InvalidAmountException;
    void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException;
}
