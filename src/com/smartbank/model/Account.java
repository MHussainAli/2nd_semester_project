package com.smartbank.model;

import com.smartbank.exceptions.InsufficientBalanceException;
import com.smartbank.exceptions.InvalidAmountException;
import com.smartbank.interfaces.TransactionOperations;
import com.smartbank.interfaces.Transferable;

import java.io.Serializable;

public abstract class Account implements TransactionOperations, Transferable, Serializable {
    private static final long serialVersionUID = 1L;
    private static int accountCounter = 1000;

    private final String accountNumber;
    private String accountHolder;
    protected double balance;
    private boolean active;

    protected Account(String accountHolder, double openingBalance) throws InvalidAmountException {
        if (accountHolder == null || accountHolder.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be empty.");
        }
        if (openingBalance < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative.");
        }
        this.accountNumber = "SB" + (++accountCounter);
        this.accountHolder = accountHolder.trim();
        this.balance = openingBalance;
        this.active = true;
    }

    public abstract String getAccountType();
    public abstract double calculateInterest();

    @Override
    public void deposit(double amount) throws InvalidAmountException {
        validateAmount(amount);
        balance += amount;
    }

    @Override
    public void withdraw(double amount)
            throws InvalidAmountException, InsufficientBalanceException {
        validateAmount(amount);
        if (amount > balance) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }
        balance -= amount;
    }

    @Override
    public void transfer(Account receiver, double amount)
            throws InvalidAmountException, InsufficientBalanceException {
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver account cannot be null.");
        }
        withdraw(amount);
        receiver.deposit(amount);
    }

    protected void validateAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
    }

    public final String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        if (accountHolder == null || accountHolder.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be empty.");
        }
        this.accountHolder = accountHolder.trim();
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public void closeAccount() {
        active = false;
    }

    public static int getAccountCounter() {
        return accountCounter;
    }

    public static void updateCounterFromAccountNumber(String number) {
        if (number == null || !number.startsWith("SB")) return;
        try {
            int value = Integer.parseInt(number.substring(2));
            if (value > accountCounter) accountCounter = value;
        } catch (NumberFormatException ignored) {
        }
    }

    @Override
    public String toString() {
        return accountNumber + " | " + accountHolder + " | " + getAccountType()
                + " | Balance: Rs. " + String.format("%.2f", balance);
    }
}
