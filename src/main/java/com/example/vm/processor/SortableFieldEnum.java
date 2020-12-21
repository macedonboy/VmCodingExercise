package com.example.vm.processor;

import com.example.vm.domain.Transaction;

import java.util.function.Function;

/**
 * A Enum to return a Comparable of a Transaction field for sorting
 */
public enum SortableFieldEnum {

    TRANSACTION_DATE("transactionDate", t -> (Comparable)t.getTransactionDate()),
    VENDOR("vendor", t -> (Comparable)t.getVendor()),
    TYPE("type", t -> (Comparable)t.getType()),
    AMOUNT("amount", t -> (Comparable)t.getAmount()),
    CATEGORY("category", t -> (Comparable)t.getCategory());

    private String fieldName;
    private Function<Transaction, Comparable<Object>> extractor;

    private SortableFieldEnum(String name, Function<Transaction, Comparable<Object>> extractor) {
        this.fieldName = name;
        this.extractor = extractor;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static SortableFieldEnum getEnum(String value) {
        for(SortableFieldEnum v : values()) {
            if (v.getFieldName().equals(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }

    public Function<Transaction, Comparable<Object>> getExtractor() {
        return extractor;
    }
}
