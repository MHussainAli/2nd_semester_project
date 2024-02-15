package com.smartbank.model;

import com.smartbank.exceptions.InvalidAmountException;

public class StudentAccount extends Account {
    private static final long serialVersionUID = 1L;
    private static final double INTEREST_RATE = 0.04;

    public StudentAccount(String accountHolder, double openingBalance) throws InvalidAmountException {
        super(accountHolder, openingBalance);
    }

    @Override
    public String getAccountType() {
        return "Student";
    }

    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    }
}
