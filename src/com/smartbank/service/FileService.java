package com.smartbank.service;

import java.io.*;

public class FileService {
    private static final String FILE_NAME = "data/bank_data.dat";

    public void save(BankService bankService) throws IOException {
        File file = new File(FILE_NAME);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(bankService);
        }
    }

    public BankService load() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new BankService();

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            return (BankService) input.readObject();
        }
    }
}
