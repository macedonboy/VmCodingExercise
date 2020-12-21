package com.example.vm.domain;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    @CsvDate(value = "yyyy-MM-dd")
    @CsvBindByPosition(position = 0)
    private LocalDate transactionDate;

    @CsvBindByPosition(position = 1)
    private String vendor;

    @CsvBindByPosition(position = 2)
    private String type;

    @CsvBindByPosition(position = 3)
    private BigDecimal amount;

    @CsvBindByPosition(position = 4)
    private String category;

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
