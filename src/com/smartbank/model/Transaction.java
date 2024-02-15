package com.smartbank.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String transactionId;
    private final TransactionType type;
    private final String accountNumber;
    private final String relatedAccount;
    private final double amount;
    private final LocalDateTime dateTime;

    public Transaction(String transactionId, TransactionType type, String accountNumber,
                       String relatedAccount, double amount) {
        this.transactionId = transactionId;
        this.type = type;
        this.accountNumber = accountNumber;
        this.relatedAccount = relatedAccount;
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
    }

    public String getTransactionId() { return transactionId; }
    public TransactionType getType() { return type; }
    public String getAccountNumber() { return accountNumber; }
    public String getRelatedAccount() { return relatedAccount; }
    public double getAmount() { return amount; }
    public LocalDateTime getDateTime() { return dateTime; }

    @Override
    public String toString() {
        String related = relatedAccount == null || relatedAccount.isEmpty() ? "-" : relatedAccount;
        return String.format("%s | %s | %s | Rs. %.2f | %s | %s",
                transactionId, type, accountNumber, amount, related,
                dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
    }
}
