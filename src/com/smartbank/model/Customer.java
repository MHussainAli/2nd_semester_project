package com.smartbank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String customerId;
    private String name;
    private String phone;
    private String email;
    private final List<String> accountNumbers;

    // Overloaded constructor
    public Customer(String customerId, String name) {
        this(customerId, name, "", "");
    }

    public Customer(String customerId, String name, String phone, String email) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.accountNumbers = new ArrayList<>();
    }

    public void addAccount(String accountNumber) {
        if (!accountNumbers.contains(accountNumber)) accountNumbers.add(accountNumber);
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public List<String> getAccountNumbers() { return accountNumbers; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return customerId + " | " + name + " | " + phone + " | " + email;
    }
}
