package com.shevchenko.csvsummary.component.impl;

import com.shevchenko.csvsummary.component.Parser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author Dmytro_Shevchenko4
 */
@Component
public class ParserImpl implements Parser{

    @Override
    public String parseHeaders(File file) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            CSVFormat format = CSVFormat.DEFAULT.withHeader();
            CSVParser parser = new CSVParser(fileReader, format);
            return parser.getHeaderMap().keySet().stream().collect(Collectors.joining(", "));
        }
    }

    @Override
    public Double summarize(File file, String header) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            CSVFormat format = CSVFormat.DEFAULT.withHeader();
            CSVParser parser = new CSVParser(fileReader, format);
            return parser.getRecords().stream().mapToDouble(csvRecord -> Double.parseDouble(csvRecord.get(header))).sum();
        }
    }
}
