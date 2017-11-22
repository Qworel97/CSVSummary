package com.shevchenko.csvsummary.util;

import java.util.UUID;

/**
 * @author Dmytro_Shevchenko4
 */
public class NameUtils {
    public static String generateUniqueFileName(String fileName) {
        return String.format("%s%s", UUID.randomUUID(), fileName);
    }
}
