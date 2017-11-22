package com.shevchenko.csvsummary.component;

import java.io.File;
import java.io.IOException;

/**
 * @author Dmytro_Shevchenko4
 */
public interface Parser {

    String parseHeaders(File file) throws IOException;

    Double summarize(File file, String header) throws IOException;
}
