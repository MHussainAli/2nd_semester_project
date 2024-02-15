package com.smartbank.gui;

import com.smartbank.model.Account;
import com.smartbank.service.BankService;
import com.smartbank.service.FileService;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private static BankService bankService;
    private final FileService fileService = new FileService();
    private final JLabel accountsLabel = new JLabel();
    private final JLabel customersLabel = new JLabel();
    private final JLabel balanceLabel = new JLabel();

    public DashboardFrame() {
        if (bankService == null) {
            try {
                bankService = fileService.load();
            } catch (Exception e) {
                bankService = new BankService();
                JOptionPane.showMessageDialog(null, "Could not load saved data. Starting with empty data.");
            }
        }

        setTitle("Smart Bank System - Dashboard");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        updateDashboard();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save Data");
        JMenuItem exitItem = new JMenuItem("Exit");
        saveItem.addActionListener(e -> saveData());
        exitItem.addActionListener(e -> exitApplication());
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu accountMenu = new JMenu("Account");
        JMenuItem createItem = new JMenuItem("Create Account");
        JMenuItem closeItem = new JMenuItem("Close Account");
        createItem.addActionListener(e -> new AccountFrame(this).setVisible(true));
        closeItem.addActionListener(e -> closeAccount());
        accountMenu.add(createItem);
        accountMenu.add(closeItem);
        menuBar.add(accountMenu);
        setJMenuBar(menuBar);

        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("SMART BANK DASHBOARD");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        header.add(title, BorderLayout.WEST);

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        header.add(logout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 3, 15, 15));
        cards.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        cards.add(createCard("Customers", customersLabel));
        cards.add(createCard("Active Accounts", accountsLabel));
        cards.add(createCard("Total Balance", balanceLabel));

        JPanel center = new JPanel(new BorderLayout());
        center.add(cards, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(2, 3, 15, 15));
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        JButton create = new JButton("Create Account");
        JButton deposit = new JButton("Deposit");
        JButton withdraw = new JButton("Withdraw");
        JButton transfer = new JButton("Transfer");
        JButton transactions = new JButton("Transactions");
        JButton accounts = new JButton("View Accounts");

        create.addActionListener(e -> new AccountFrame(this).setVisible(true));
        deposit.addActionListener(e -> transactionDialog("Deposit"));
        withdraw.addActionListener(e -> transactionDialog("Withdraw"));
        transfer.addActionListener(e -> transactionDialog("Transfer"));
        transactions.addActionListener(e -> showTransactions());
        accounts.addActionListener(e -> showAccounts());

        buttons.add(create);
        buttons.add(deposit);
        buttons.add(withdraw);
        buttons.add(transfer);
        buttons.add(transactions);
        buttons.add(accounts);

        center.add(buttons, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        add(new JLabel("  File-based Java OOP Banking Application"), BorderLayout.SOUTH);
    }

    private JPanel createCard(String title, JLabel value) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        value.setHorizontalAlignment(SwingConstants.CENTER);
        value.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(value, BorderLayout.CENTER);
        return panel;
    }

    private void transactionDialog(String type) {
        String account = JOptionPane.showInputDialog(this, "Enter account number:");
        if (account == null) return;

        if (type.equals("Transfer")) {
            String receiver = JOptionPane.showInputDialog(this, "Enter receiver account number:");
            if (receiver == null) return;
            String amountText = JOptionPane.showInputDialog(this, "Enter transfer amount:");
            if (amountText == null) return;
            try {
                double amount = Double.parseDouble(amountText);
                bankService.transfer(account.trim(), receiver.trim(), amount);
                saveData();
                JOptionPane.showMessageDialog(this, "Transfer successful.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Transaction Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            String amountText = JOptionPane.showInputDialog(this, "Enter amount:");
            if (amountText == null) return;
            try {
                double amount = Double.parseDouble(amountText);
                if (type.equals("Deposit")) bankService.deposit(account.trim(), amount);
                else bankService.withdraw(account.trim(), amount);
                saveData();
                JOptionPane.showMessageDialog(this, type + " successful.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Transaction Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        updateDashboard();
    }

    private void showAccounts() {
        StringBuilder text = new StringBuilder();
        for (Account account : bankService.getAccounts()) {
            if (account.isActive()) text.append(account).append("\n");
        }
        JTextArea area = new JTextArea(text.length() == 0 ? "No active accounts." : text.toString(), 15, 70);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Accounts", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showTransactions() {
        StringBuilder text = new StringBuilder();
        for (com.smartbank.model.Transaction transaction : bankService.getTransactions()) text.append(transaction).append("\n");
        JTextArea area = new JTextArea(text.length() == 0 ? "No transactions." : text.toString(), 18, 90);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Transaction History", JOptionPane.INFORMATION_MESSAGE);
    }

    private void closeAccount() {
        String account = JOptionPane.showInputDialog(this, "Enter account number to close:");
        if (account == null) return;
        try {
            bankService.closeAccount(account.trim());
            saveData();
            updateDashboard();
            JOptionPane.showMessageDialog(this, "Account closed successfully.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateDashboard() {
        int active = 0;
        for (Account account : bankService.getAccounts()) if (account.isActive()) active++;
        customersLabel.setText(String.valueOf(bankService.getCustomers().size()));
        accountsLabel.setText(String.valueOf(active));
        balanceLabel.setText(String.format("Rs. %.2f", bankService.getTotalBalance()));
    }

    private void saveData() {
        try {
            fileService.save(bankService);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not save data: " + e.getMessage());
        }
    }

    private void exitApplication() {
        saveData();
        System.exit(0);
    }

    public static BankService getBankService() {
        return bankService;
    }
}
