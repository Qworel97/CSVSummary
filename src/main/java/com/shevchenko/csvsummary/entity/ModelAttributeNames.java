package com.shevchenko.csvsummary.entity;

/**
 * @author Dmytro_Shevchenko4
 */
public enum ModelAttributeNames {
    ERROR("error"),
    SUMMARY("summary"),
    MESSAGE("message"),
    HEADERS("headers");

    private String name;

    ModelAttributeNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
