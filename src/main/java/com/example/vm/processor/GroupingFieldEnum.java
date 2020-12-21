package com.example.vm.processor;

import com.example.vm.domain.Transaction;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * A Enum to return an extractor of a Transaction field used for grouping
 */
public enum GroupingFieldEnum {

    CATEGORY("category", t -> StringUtils.isBlank(t.getCategory()) ? "" : t.getCategory());

    private String fieldName;
    private Function<Transaction, String> extractor;

    private GroupingFieldEnum(String name, Function<Transaction, String> extractor) {
        this.fieldName = name;
        this.extractor = extractor;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static GroupingFieldEnum getEnum(String value) {
        for(GroupingFieldEnum v : values()) {
            if (v.getFieldName().equals(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }

    public Function<Transaction, String> getExtractor() {
        return extractor;
    }
}