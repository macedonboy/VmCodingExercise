package com.example.vm;

import com.example.vm.domain.Transaction;
import com.example.vm.repository.VMCSVReader;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTest {

    protected VMCSVReader reader;
    protected List<Transaction> transactions;

    @BeforeEach
    public void setup() throws FileNotFoundException {
        reader = new VMCSVReader();
        transactions = reader.mapToCSV("bigger_input.csv", Transaction.class);
    }

    public void assertListIsOrderedDescending(List<Transaction> transactions) {
        for (int i = 0; i < transactions.size() - 1; i++) {
            assertTrue(transactions.get(i).getTransactionDate().isAfter(transactions.get(i + 1).getTransactionDate())
                    || transactions.get(i).getTransactionDate().equals(transactions.get(i + 1).getTransactionDate())
            );
        }
    }
}
