package com.example.vm.processor;

import com.example.vm.BaseTest;
import com.example.vm.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * These tests are mainly to these that the predicates work when passed a Processor
 */
public class AdditionalProcessorTests extends BaseTest {

    private BasicProcessor processor;
    private Predicate<Transaction> groceriesCatFilter;
    private Predicate<Transaction> nullCatFilter;

    @BeforeEach
    public void setup() throws FileNotFoundException {
        super.setup();
        processor = new BasicProcessor();
        groceriesCatFilter = t -> "Groceries".equals(t.getCategory());
        nullCatFilter = t -> Objects.isNull(t.getCategory());
    }

    @Test
    public void testEmptyLists() {
        List<Transaction> filtered = processor.transactionsForDescending(Collections.emptyList(), Arrays.asList(groceriesCatFilter), SortableFieldEnum.TRANSACTION_DATE);
        assertTrue(filtered.isEmpty());
    }

    @Test
    public void testNullLists() {
        List<Transaction> filtered = processor.transactionsForDescending(Collections.emptyList(), null, SortableFieldEnum.TRANSACTION_DATE);
        assertTrue(filtered.isEmpty());
    }

    @Test
    public void testNullPredicates() {
        List<Transaction> filtered = processor.transactionsForDescending(Collections.emptyList(), null, SortableFieldEnum.TRANSACTION_DATE);
        assertTrue(filtered.isEmpty());
    }

    @Test
    public void testNullPredicatesOnList() {
        List<Transaction> filtered = processor.transactionsForDescending(transactions, null, SortableFieldEnum.TRANSACTION_DATE);
        assertEquals(transactions.size(), filtered.size());
    }

    @Test
    public void testTransactionsForAGivenCategoryIsSorted() {
        List<Transaction> filtered = processor.transactionsForDescending(transactions, Arrays.asList(groceriesCatFilter), SortableFieldEnum.TRANSACTION_DATE);
        assertListIsOrderedDescending(filtered);
    }

    @Test
    public void testTransactionsForGroceries() {
        List<Transaction> filtered = processor.transactionsForDescending(transactions, Arrays.asList(groceriesCatFilter), SortableFieldEnum.TRANSACTION_DATE);
        assertTrue(testForCategory(filtered, "Groceries"));
    }

    @Test
    public void testTransactionsForUnclassified() {
        List<Transaction> filtered = processor.transactionsForDescending(transactions, Arrays.asList(nullCatFilter), SortableFieldEnum.TRANSACTION_DATE);
        assertEquals(3, filtered.size());
    }

    private boolean testForCategory(List<Transaction> transactions, String category) {

        for (int i = 0; i < transactions.size(); i++) {
            if (!transactions.get(i).getCategory().equals(category)) {
                return false;
            }
        }

        return true;
    }
}