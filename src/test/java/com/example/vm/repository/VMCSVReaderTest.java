package com.example.vm.repository;

import com.example.vm.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VMCSVReaderTest {

    private VMCSVReader reader;

    @BeforeEach
    public void setup() {
        reader = new VMCSVReader();
    }

    @Test
    public void testReadCSVFile () {
        try {
            List<Transaction> transactions = reader.mapToCSV("one_liner.csv", Transaction.class);
            assertEquals(1, transactions.size());;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFieldMappings () {
        try {
            List<Transaction> transactions = reader.mapToCSV("one_liner.csv", Transaction.class);
            assertEquals(1, transactions.size());
            Transaction t = transactions.get(0);
            assertEquals(t.getTransactionDate(), LocalDate.of(2020, 11, 01));
            assertEquals(t.getVendor(),"Morrisons");
            assertEquals(t.getType(), "card");
            assertEquals(t.getAmount(), new BigDecimal("10.40"));
            assertEquals(t.getCategory(), "Groceries");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}