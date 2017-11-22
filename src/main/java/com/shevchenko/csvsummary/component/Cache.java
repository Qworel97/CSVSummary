package com.shevchenko.csvsummary.component;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Simple cache interface
 *
 * @author Dmytro_Shevchenko4
 */
public interface Cache {
    /**
     * Puts file to cache. File should be remove after eviction
     * @param file that should be cached
     */
    void put(File file);

    /**
     * Retrieves file from cache. Extends its life time
     * @param name file name
     * @return file that was found in cache
     * @throws FileNotFoundException if file is not in cache
     */
    File get(String name) throws FileNotFoundException;

    /**
     * Extends life time for file with specified name
     * @param name file name
     * @return true if file exists
     */
    boolean extend(String name);
}
