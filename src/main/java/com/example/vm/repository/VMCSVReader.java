package com.example.vm.repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

public class VMCSVReader {

    public <T> List<T> mapToCSV(String filename, Class<T> mapToClass) throws FileNotFoundException {

        URL resource = getClass().getClassLoader().getResource(filename);
        Reader reader = new FileReader(resource.getFile());

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                .withSkipLines(1)
                .build();
        ColumnPositionMappingStrategy<T> strategy = new ColumnPositionMappingStrategy<T>();
        strategy.setType(mapToClass);
        strategy.setColumnMapping(new String[]{"transactionDate"});

        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader)
                .withMappingStrategy(strategy)
                .build();

        return csvToBean.parse();
    }
}
