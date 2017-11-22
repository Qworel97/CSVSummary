package com.shevchenko.csvsummary.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

/**
 * @author Dmytro_Shevchenko4
 */
public interface Cache {
    void put(File file);
    File get(String name) throws FileNotFoundException;
    boolean extend(String name);
}
