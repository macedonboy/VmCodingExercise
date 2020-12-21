package com.example.vm.processor;

import java.math.BigDecimal;
import java.util.DoubleSummaryStatistics;
import java.util.function.Function;

/**
 * A Enum to return an extractor to return statistical value from
 * DoubleSummaryStatistics
 */
public enum SummarizingEnum {
    MIN(s -> BigDecimal.valueOf(s.getMin()).setScale(2)),
    MAX(s -> BigDecimal.valueOf(s.getMax()).setScale(2));

    private Function<DoubleSummaryStatistics, BigDecimal> extractor;

    private SummarizingEnum(Function<DoubleSummaryStatistics, BigDecimal> extractor) {
        this.extractor = extractor;
    }

    public Function<DoubleSummaryStatistics, BigDecimal> getExtractor() {
        return extractor;
    }
}
