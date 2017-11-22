package com.shevchenko.csvsummary.component;

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
public class Parser {

    public String parseHeaders(File file) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            CSVFormat format = CSVFormat.DEFAULT.withHeader();
            CSVParser parser = new CSVParser(fileReader, format);
            return parser.getHeaderMap().keySet().stream().collect(Collectors.joining(", "));
        }
    }
}
