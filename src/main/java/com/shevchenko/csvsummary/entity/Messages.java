package com.shevchenko.csvsummary.entity;

/**
 * @author Dmytro_Shevchenko4
 */
public enum Messages {
    ERROR_WHILE_PARSING("Error while parsing"),
    ERROR_WHILE_UPLOADING("Error while uploading"),
    NO_COOKIE_ERROR("No file is attached to current session. Upload first"),
    FILE_WAS_NOT_FOUND("You file was not found. Maybe it expired"),
    NO_FILE_SELECTED_ERROR("No file selected"),
    FILE_CONTAINS_NOT_DOUBLE_ERROR("File contains wrong data that can not be converted to double"),
    NOT_EXISTING_HEADER("This header does not exist. Try one from list below"),
    SUCCESSFULLY_UPLOADED_FILE("You have successfully uploaded file");

    private String text;

    Messages(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
