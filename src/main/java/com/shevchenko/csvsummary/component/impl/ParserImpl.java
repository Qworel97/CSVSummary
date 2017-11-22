package com.shevchenko.csvsummary.component.impl;

import com.shevchenko.csvsummary.component.Parser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang3.math.NumberUtils;
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

    public boolean isValid(File file) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            CSVFormat format = CSVFormat.DEFAULT.withHeader();
            try {
                CSVParser parser = new CSVParser(fileReader, format);
                boolean result = parser.getRecords().stream().allMatch(csvRecord -> csvRecord.toMap().values().stream().allMatch(value -> NumberUtils.isNumber(value)));
                LOGGER.debug("File is valid -> {} ", file.getName());
                return result;
            } catch (IllegalArgumentException e) {
                LOGGER.debug("File is not valid -> {} ", file.getName());
                return false;
            }
        }
    }
}
