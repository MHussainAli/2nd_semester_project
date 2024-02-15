package com.smartbank.service;

import com.smartbank.exceptions.AccountNotFoundException;
import com.smartbank.exceptions.InsufficientBalanceException;
import com.smartbank.exceptions.InvalidAmountException;
import com.smartbank.interfaces.Transferable;
import com.smartbank.model.*;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BankService implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Customer> customers = new ArrayList<>();
    private final List<Account> accounts = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();

    public Customer addCustomer(String name, String phone, String email) {
        String id = "C" + String.format("%04d", customers.size() + 1);
        Customer customer = new Customer(id, name, phone, email);
        customers.add(customer);
        return customer;
    }

    public Account createAccount(String customerId, AccountType type, double openingBalance)
            throws InvalidAmountException {
        Customer customer = findCustomer(customerId);
        if (customer == null) throw new IllegalArgumentException("Customer not found.");

        Account account;
        switch (type) {
            case SAVINGS:
                account = new SavingsAccount(customer.getName(), openingBalance);
                break;
            case CURRENT:
                account = new CurrentAccount(customer.getName(), openingBalance);
                break;
            case STUDENT:
                account = new StudentAccount(customer.getName(), openingBalance);
                break;
            default:
                throw new IllegalArgumentException("Invalid account type.");
        }

        accounts.add(account);
        customer.addAccount(account.getAccountNumber());

        if (openingBalance > 0) {
            addTransaction(TransactionType.DEPOSIT, account.getAccountNumber(), null, openingBalance);
        }
        return account;
    }

    public void deposit(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException {
        Account account = getAccount(accountNumber);
        account.deposit(amount);
        addTransaction(TransactionType.DEPOSIT, accountNumber, null, amount);
    }

    public void withdraw(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientBalanceException {
        Account account = getAccount(accountNumber);
        account.withdraw(amount);
        addTransaction(TransactionType.WITHDRAW, accountNumber, null, amount);
    }

    public void transfer(String fromAccount, String toAccount, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientBalanceException {
        Account sender = getAccount(fromAccount);
        Account receiver = getAccount(toAccount);

        if (fromAccount.equals(toAccount)) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same.");
        }

        sender.transfer(receiver, amount);

        addTransaction(TransactionType.TRANSFER, fromAccount, toAccount, amount);
        addTransaction(TransactionType.TRANSFER, toAccount, fromAccount, amount);
    }

    public void closeAccount(String accountNumber) throws AccountNotFoundException {
        Account account = getAccount(accountNumber);
        account.closeAccount();
    }

    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        for (Account account : accounts) {
            if (account.getAccountNumber().equalsIgnoreCase(accountNumber) && account.isActive()) {
                return account;
            }
        }
        throw new AccountNotFoundException("Active account not found: " + accountNumber);
    }

    public Customer findCustomer(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equalsIgnoreCase(customerId)) return customer;
        }
        return null;
    }

    public List<Customer> getCustomers() { return customers; }
    public List<Account> getAccounts() { return accounts; }
    public List<Transaction> getTransactions() { return transactions; }

    public List<Transaction> getTransactionsForAccount(String accountNumber) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                result.add(transaction);
            }
        }
        return result;
    }

    public double getTotalBalance() {
        double total = 0;
        for (Account account : accounts) {
            if (account.isActive()) total += account.getBalance();
        }
        return total;
    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        for (Account account : accounts) {
            Account.updateCounterFromAccountNumber(account.getAccountNumber());
        }
    }

    private void addTransaction(TransactionType type, String accountNumber,
                                String relatedAccount, double amount) {
        String id = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        transactions.add(new Transaction(id, type, accountNumber, relatedAccount, amount));
    }
}
