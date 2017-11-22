package com.shevchenko.csvsummary.component.impl;

import com.shevchenko.csvsummary.component.Parser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author Dmytro_Shevchenko4
 */
@Component
public class ParserImpl implements Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserImpl.class);

    @Override
    public String parseHeaders(File file) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            CSVFormat format = CSVFormat.DEFAULT.withHeader();
            CSVParser parser = new CSVParser(fileReader, format);
            String result = parser.getHeaderMap().keySet().stream().collect(Collectors.joining(", "));
            LOGGER.debug("Headers were parsed for file -> {}", file.getName());
            return result;
        }
    }

    @Override
    public Double summarize(File file, String header) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            CSVFormat format = CSVFormat.DEFAULT.withHeader();
            CSVParser parser = new CSVParser(fileReader, format);
            Double result = parser.getRecords().stream().mapToDouble(csvRecord -> Double.parseDouble(csvRecord.get(header))).sum();
            LOGGER.debug("File -> {} Header -> {} ", file.getName(), header);
            return result;
        }
    }
}
