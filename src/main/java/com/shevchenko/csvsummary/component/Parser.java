package com.shevchenko.csvsummary.component;

import java.io.File;
import java.io.IOException;

/**
 * Interface for CSV parser
 *
 * @author Dmytro_Shevchenko4
 */
public interface Parser {

    /**
     * Parses headers of file
     *
     * @param file file to parse
     * @return string that contains all headers
     * @throws IOException if file is not found
     */
    String parseHeaders(File file) throws IOException;

    /**
     * Return sum of row with specific header
     * @param file   file to sum
     * @param header header name
     * @return sum of a row
     * @throws IOException if file not found
     * @throws NumberFormatException if some value is not double
     * @throws IllegalArgumentException if header is not present
     */
    Double summarize(File file, String header) throws IOException, NumberFormatException, IllegalArgumentException;

    /**
     * Checks if file contains valid double data
     * @param file to check
     * @return true if all (except headers) are doubles
     * @throws IOException if file is not found
     */
    boolean isValid(File file) throws IOException;
}
