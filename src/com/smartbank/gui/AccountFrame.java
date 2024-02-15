package com.smartbank.gui;

import com.smartbank.model.AccountType;
import com.smartbank.service.BankService;

import javax.swing.*;
import java.awt.*;

public class AccountFrame extends JDialog {
    private final JTextField nameField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JComboBox<AccountType> typeBox = new JComboBox<>(AccountType.values());
    private final JTextField openingBalanceField = new JTextField("0");
    private final DashboardFrame dashboard;

    public AccountFrame(DashboardFrame dashboard) {
        super(dashboard, "Create Bank Account", true);
        this.dashboard = dashboard;

        setSize(500, 350);
        setLocationRelativeTo(dashboard);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("CREATE NEW ACCOUNT", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 25, 10, 25));
        form.add(new JLabel("Customer Name:"));
        form.add(nameField);
        form.add(new JLabel("Phone:"));
        form.add(phoneField);
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Account Type:"));
        form.add(typeBox);
        form.add(new JLabel("Opening Balance:"));
        form.add(openingBalanceField);
        add(form, BorderLayout.CENTER);

        JButton createButton = new JButton("Create Account");
        createButton.addActionListener(e -> createAccount());
        add(createButton, BorderLayout.SOUTH);
    }

    private void createAccount() {
        try {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            double opening = Double.parseDouble(openingBalanceField.getText().trim());

            if (name.isEmpty()) throw new IllegalArgumentException("Name is required.");
            if (phone.isEmpty()) throw new IllegalArgumentException("Phone is required.");
            if (email.isEmpty()) throw new IllegalArgumentException("Email is required.");

            BankService service = DashboardFrame.getBankService();
            var customer = service.addCustomer(name, phone, email);
            var account = service.createAccount(customer.getCustomerId(),
                    (AccountType) typeBox.getSelectedItem(), opening);

            JOptionPane.showMessageDialog(this,
                    "Account created successfully!\nAccount Number: " + account.getAccountNumber(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            dashboard.updateDashboard();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
