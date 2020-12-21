package com.example.vm.processor;

import com.example.vm.domain.Transaction;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BasicProcessor {

    /**
     * Run all filter predicates and return true if all predicates return true.
     */
    public Stream<Transaction> filter(List<Transaction> transactions, Collection<Predicate<Transaction>> filters) {
        if (Objects.isNull(transactions)) {
            return Stream.empty();
        }

        if (Objects.isNull(filters) || filters.isEmpty()) {
            return transactions.stream();
        }

        return transactions.stream()
                .parallel()
                .filter(filters.stream().reduce(p -> true, Predicate::and));
    }

    public List<Transaction> transactionsForDescending(List<Transaction> transactions, Collection<Predicate<Transaction>> filters, SortableFieldEnum sortField) {

        return transactionsFor(transactions, filters, sortField, SortOrder.DESCENDING);
    }

    /**
     * Run predicates on transactions and sort the returned list
     */
    public List<Transaction> transactionsFor(List<Transaction> transactions, Collection<Predicate<Transaction>> filters, SortableFieldEnum sortField, SortOrder order) {

        Stream<Transaction> stream = filter(transactions, filters);
        if (sortField != null) {
            Comparator<Transaction> comparator = Comparator.comparing(sortField.getExtractor());
            if (order != null && order == SortOrder.DESCENDING) {
                comparator = comparator.reversed();
            }

            stream = stream
                    .sorted(comparator);
        }

        return stream.collect(Collectors.toList());
    }

    /**
     * Group transactions based on a field defined by a GroupingFieldEnum and
     * return the sum of the values
     */
    public Map<String, BigDecimal> totalOutgoingGroupingBy(List<Transaction> transactions, GroupingFieldEnum grouping) {

        return transactions
                .stream()
                .collect(
                        Collectors.groupingBy(grouping.getExtractor(),
                                Collectors.mapping(Transaction::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)))
                );
    }

    /**
     * Group transactions based on YearMonth and return the average of the values
     */
    public Map<YearMonth, BigDecimal> monthlyAverageSpend(List<Transaction> transactions, Collection<Predicate<Transaction>> filters) {

        Stream<Transaction> finalStream = filter(transactions, filters);
        return finalStream
                .collect(
                        Collectors.groupingBy(f -> YearMonth.from(f.getTransactionDate()),
                                Collectors.mapping(Transaction::getAmount, new BigDecimalCollector())
                        )
                );
    }

    /**
     * Returns a statistical value based on the filtered transactions
     */
    public Optional<BigDecimal> doubleSummaryStatistics(List<Transaction> transactions, Collection<Predicate<Transaction>> filters, SummarizingEnum summarizingEnum) {

        DoubleSummaryStatistics stats = filter(transactions, filters)
                .map(t -> t.getAmount())
                .collect(Collectors.summarizingDouble
                        (BigDecimal::doubleValue));

        if (stats.getCount() == 0) {
            return Optional.empty();
        }

        return Optional.of(summarizingEnum.getExtractor().apply(stats));
    }
}