package com.example.vm.processor;

import com.example.vm.BaseTest;
import com.example.vm.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BasicProcessorTest extends BaseTest {

    private BasicProcessor processor;
    private Predicate<Transaction> groceriesCatFilter;
    private Predicate<Transaction> myMonthlyDDCatFilter;
    private Predicate<Transaction> nullCatFilter;
    private Predicate<Transaction> startOf2020Filter;
    private Predicate<Transaction> endOf2020Filter;
    private Predicate<Transaction> startOf2019Filter;
    private Predicate<Transaction> endOf2019Filter;

    @BeforeEach
    public void setup() throws FileNotFoundException {
        super.setup();
        processor = new BasicProcessor();
        groceriesCatFilter = t -> "Groceries".equals(t.getCategory());
        myMonthlyDDCatFilter = t -> "MyMonthlyDD".equals(t.getCategory());
        nullCatFilter = t -> Objects.isNull(t.getCategory());

        startOf2020Filter = t -> t.getTransactionDate().compareTo(LocalDate.of(2020, 1, 1)) >= 0;
        endOf2020Filter = t -> t.getTransactionDate().compareTo(LocalDate.of(2020, 12, 31)) < 0;
        startOf2019Filter = t -> t.getTransactionDate().compareTo(LocalDate.of(2019, 1, 1)) >= 0;
        endOf2019Filter = t -> t.getTransactionDate().compareTo(LocalDate.of(2019, 12, 31)) < 0;
    }

    /**
     * Scenario 1
     * All transactions for a given category (Groceries) - latest first
     */
    @Test
    public void testTransactionsForGroceriesLatestFirst() {

        List<Transaction> filtered = processor.transactionsForDescending(transactions, Arrays.asList(groceriesCatFilter), SortableFieldEnum.TRANSACTION_DATE);

        assertEquals(5, filtered.size());
        assertListIsOrderedDescending(filtered);
    }

    /**
     * Scenario 1
     * All transactions for a given category (MyMonthlyDD) - latest first
     */
    @Test
    public void testTransactionsForMyMonthlyDDLatestFirst() {

        List<Transaction> filtered = processor.transactionsForDescending(transactions, Arrays.asList(myMonthlyDDCatFilter), SortableFieldEnum.TRANSACTION_DATE);

        assertEquals(4, filtered.size());
        assertListIsOrderedDescending(filtered);
    }

    /**
     * Scenario 1
     * All transactions for a given category (Unclassified) - latest first
     */
    @Test
    public void testTransactionsForAGivenCategoryLatestFirstUnclassified() {

        List<Transaction> filtered = processor.transactionsForDescending(transactions, Arrays.asList(nullCatFilter), SortableFieldEnum.TRANSACTION_DATE);

        assertEquals(3, filtered.size());
        assertListIsOrderedDescending(filtered);
    }

    /**
     * Scenario 2
     * Total outgoing per category
     * The exercise doesn't say to be constrained by year, so the results are for 2019 & 2020
     */
    @Test
    public void testTotalOutgoingPerCategoryEverything() {
        Map<String, BigDecimal> filtered = processor.totalOutgoingGroupingBy(transactions, GroupingFieldEnum.CATEGORY);

        assertEquals(new BigDecimal("45.77"), filtered.get("Groceries"));
        assertEquals(new BigDecimal("1280"), filtered.get("MyMonthlyDD"));
        assertEquals(new BigDecimal("25"), filtered.get(""));
    }

    /**
     * Scenario 3
     * Monthly average spend in a given category (Groceries)
     */
    @Test
    public void testMonthlyAverageSpendInAGivenCategoryGroceries() {

        Map<YearMonth, BigDecimal> filtered = processor.monthlyAverageSpend(transactions, Arrays.asList(groceriesCatFilter));

        assertEquals(new BigDecimal("5.99"), filtered.get(YearMonth.of(2020, 10)));
        assertEquals(new BigDecimal("10.40"), filtered.get(YearMonth.of(2020, 11)));
        assertEquals(new BigDecimal("9.49"), filtered.get(YearMonth.of(2019, 10)));
        assertEquals(new BigDecimal("10.40"), filtered.get(YearMonth.of(2019, 11)));
    }

    /**
     * Scenario 3
     * Monthly average spend in a given category (MyMonthlyDD)
     */
    @Test
    public void testMonthlyAverageSpendInAGivenCategoryMyMonthlyDD() {

        Map<YearMonth, BigDecimal> filtered = processor.monthlyAverageSpend(transactions, Arrays.asList(myMonthlyDDCatFilter));

        assertEquals(new BigDecimal("320.00"), filtered.get(YearMonth.of(2020, 10)));
        assertEquals(new BigDecimal("320.00"), filtered.get(YearMonth.of(2019, 10)));
    }

    /**
     * Scenario 3
     * Monthly average spend in a given category (Unclassified)
     */
    @Test
    public void testMonthlyAverageSpendInAGivenCategoryUnclassified() {

        Map<YearMonth, BigDecimal> filtered = processor.monthlyAverageSpend(transactions, Arrays.asList(nullCatFilter));

        assertEquals(new BigDecimal("10.00"), filtered.get(YearMonth.of(2020, 9)));
        assertEquals(new BigDecimal("7.50"), filtered.get(YearMonth.of(2019, 9)));
    }

    /**
     * Scenario 4
     * Highest spend in a given category (Groceries), for a given year (2020)
     */
    @Test
    public void testHighestSpendInGroceriesFor2020() {

        Optional<BigDecimal> od = processor.doubleSummaryStatistics(transactions, Arrays.asList(groceriesCatFilter, startOf2020Filter, endOf2020Filter), SummarizingEnum.MAX);

        assertEquals(new BigDecimal("10.40"), od.get());
    }

    /**
     * Scenario 4
     * Highest spend in a given category (MyMonthlyDD), for a given year (2020)
     */
    @Test
    public void testHighestSpendInMyMonthlyDDFor2020() {

        Optional<BigDecimal> od = processor.doubleSummaryStatistics(transactions, Arrays.asList(myMonthlyDDCatFilter, startOf2020Filter, endOf2020Filter), SummarizingEnum.MAX);

        assertEquals(new BigDecimal("600.00"), od.get());
    }

    /**
     * Scenario 4
     * Highest spend in a given category (Unclassified), for a given year (2020)
     */
    @Test
    public void testHighestSpendInUnclassifiedFor2020() {

        Optional<BigDecimal> od = processor.doubleSummaryStatistics(transactions, Arrays.asList(nullCatFilter, startOf2020Filter, endOf2020Filter), SummarizingEnum.MAX);

        assertEquals(new BigDecimal("10.00"), od.get());
    }

    /**
     * Scenario 4
     * Highest spend in a given category (Foo), for a given year (2020)
     */
    @Test
    public void testHighestSpendInFooFor2020() {

        Predicate<Transaction> p = t -> "foo".equals(t.getCategory());
        Optional<BigDecimal> od = processor.doubleSummaryStatistics(transactions, Arrays.asList(p, startOf2020Filter, endOf2020Filter), SummarizingEnum.MAX);

        assertFalse(od.isPresent());
    }

    /**
     * Scenario 4
     * Highest spend in a given category (Groceries), for a given year (2019)
     */
    @Test
    public void testHighestSpendInGroceriesFor2019() {

        Optional<BigDecimal> od = processor.doubleSummaryStatistics(transactions, Arrays.asList(groceriesCatFilter, startOf2019Filter, endOf2019Filter), SummarizingEnum.MAX);

        assertEquals(new BigDecimal("11.99"), od.get());
    }

    /**
     * Scenario 4
     * Highest spend in a given category (MyMonthlyDD), for a given year (2019)
     */
    @Test
    public void testHighestSpendInMyMonthlyDDFor2019() {

        Optional<BigDecimal> od = processor.doubleSummaryStatistics(transactions, Arrays.asList(myMonthlyDDCatFilter, startOf2019Filter, endOf2019Filter), SummarizingEnum.MAX);

        assertEquals(new BigDecimal("600.00"), od.get());
    }

    /**
     * Scenario 4
     * Highest spend in a given category (Unclassified), for a given year (2019)
     */
    @Test
    public void testHighestSpendInUnclassifiedFor2019() {

        Optional<BigDecimal> od = processor.doubleSummaryStatistics(transactions, Arrays.asList(nullCatFilter, startOf2019Filter, endOf2019Filter), SummarizingEnum.MAX);

        assertEquals(new BigDecimal("10.00"), od.get());
    }

    /**
     * Scenario 5
     * Lowest spend in a given category (Groceries), for a given year (2020)
     */
    @Test
    public void testLowestSpendInAGroceriesForA2020() {

        Optional<BigDecimal> od = processor.doubleSummaryStatistics(transactions, Arrays.asList(groceriesCatFilter, startOf2020Filter, endOf2020Filter), SummarizingEnum.MIN);

        assertEquals(new BigDecimal("5.99"), od.get());
    }

    /**
     * Scenario 5
     * Lowest spend in a given category (MyMonthlyDD), for a given year (2020)
     */
    @Test
    public void testLowestSpendInAMyMonthlyDDForA2020() {

        Optional<BigDecimal> od = processor.doubleSummaryStatistics(transactions, Arrays.asList(myMonthlyDDCatFilter, startOf2020Filter, endOf2020Filter), SummarizingEnum.MIN);

        assertEquals(new BigDecimal("40.00"), od.get());
    }

    /**
     * Scenario 5
     * Lowest spend in a given category (Unclassified), for a given year (2020)
     */
    @Test
    public void testLowestSpendInAUnclassifiedForA2020() {

        Optional<BigDecimal> od = processor.doubleSummaryStatistics(transactions, Arrays.asList(nullCatFilter, startOf2020Filter, endOf2020Filter), SummarizingEnum.MIN);

        assertEquals(new BigDecimal("10.00"), od.get());
    }

    /**
     * Scenario 5
     * Lowest spend in a given category (Groceries), for a given year (2019)
     */
    @Test
    public void testLowestSpendInGroceriesFor2019() {

        Optional<BigDecimal> od = processor.doubleSummaryStatistics(transactions, Arrays.asList(groceriesCatFilter, startOf2019Filter, endOf2019Filter), SummarizingEnum.MIN);

        assertEquals(new BigDecimal("6.99"), od.get());
    }

    /**
     * Scenario 5
     * Lowest spend in a given category (MyMonthlyDD), for a given year (2019)
     */
    @Test
    public void testLowestSpendInMyMonthlyDDFor2019() {

        Optional<BigDecimal> od = processor.doubleSummaryStatistics(transactions, Arrays.asList(myMonthlyDDCatFilter, startOf2019Filter, endOf2019Filter), SummarizingEnum.MIN);

        assertEquals(new BigDecimal("40.00"), od.get());
    }

    /**
     * Scenario 5
     * Lowest spend in a given category (Unclassified), for a given year (2019)
     */
    @Test
    public void testLowestSpendInUnclassifiedFor2019() {

        Optional<BigDecimal> od = processor.doubleSummaryStatistics(transactions, Arrays.asList(nullCatFilter, startOf2019Filter, endOf2019Filter), SummarizingEnum.MIN);

        assertEquals(new BigDecimal("5.00"), od.get());
    }
}