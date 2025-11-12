package ua.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    public static void validateString(String value, String fieldName) {
        if (!ValidationHelper.isValidString(value)) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }
    
    public static void validateISBN(String isbn) {
        if (!ValidationHelper.isValidISBN(isbn)) {
            throw new IllegalArgumentException("Invalid ISBN format. Must be 10 or 13 digits");
        }
    }
    
    public static void validateYear(int year, String fieldName) {
        if (!ValidationHelper.isValidYear(year)) {
            throw new IllegalArgumentException(fieldName + " must be between 1000 and current year");
        }
    }
    
    public static void validateReaderId(String readerId) {
        if (!ValidationHelper.isValidReaderId(readerId)) {
            throw new IllegalArgumentException("Reader ID must be 5-10 alphanumeric characters");
        }
    }
    
    public static void validateDateRange(LocalDate start, LocalDate end, String rangeDescription) {
        if (!ValidationHelper.isDateRangeValid(start, end)) {
            throw new IllegalArgumentException("Invalid date range for " + rangeDescription);
        }
    }
    
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "N/A";
    }
}
