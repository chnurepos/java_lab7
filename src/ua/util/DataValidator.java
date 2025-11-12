package ua.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DataValidator {
    
    private static final Logger logger = Logger.getLogger(DataValidator.class.getName());
    
    public static void validateString(String value, String fieldName, List<String> errors) {
        if (value == null || value.trim().isEmpty()) {
            errors.add(fieldName + ": cannot be empty");
            logger.log(Level.WARNING, "Validation failed for {0}: cannot be empty", fieldName);
        }
    }
    
    public static void validateISBN(String isbn, List<String> errors) {
        if (isbn == null) {
            errors.add("isbn: cannot be null");
            logger.log(Level.WARNING, "Validation failed for ISBN: cannot be null");
            return;
        }
        String cleanISBN = isbn.replaceAll("[\\s-]", "");
        if (!cleanISBN.matches("\\d{10}|\\d{13}")) {
            errors.add("isbn: must be 10 or 13 digits");
            logger.log(Level.WARNING, "Validation failed for ISBN: invalid format");
        }
    }
    
    public static void validateYear(int year, String fieldName, List<String> errors) {
        int currentYear = LocalDate.now().getYear();
        if (year < 1000 || year > currentYear) {
            errors.add(fieldName + ": must be between 1000 and " + currentYear);
            logger.log(Level.WARNING, "Validation failed for {0}: invalid year {1}", new Object[]{fieldName, year});
        }
    }
    
    public static void validateReaderId(String readerId, List<String> errors) {
        if (readerId == null || readerId.trim().isEmpty()) {
            errors.add("readerId: cannot be empty");
            logger.log(Level.WARNING, "Validation failed for readerId: cannot be empty");
            return;
        }
        if (!readerId.matches("[A-Z0-9]{5,10}")) {
            errors.add("readerId: must be 5-10 alphanumeric characters (uppercase)");
            logger.log(Level.WARNING, "Validation failed for readerId: invalid format");
        }
    }
    
    public static void validateDateRange(LocalDate start, LocalDate end, String rangeDescription, List<String> errors) {
        if (start == null) {
            errors.add(rangeDescription + " start date: cannot be null");
            logger.log(Level.WARNING, "Validation failed: start date is null");
        }
        if (end == null) {
            errors.add(rangeDescription + " end date: cannot be null");
            logger.log(Level.WARNING, "Validation failed: end date is null");
        }
        if (start != null && end != null && start.isAfter(end)) {
            errors.add(rangeDescription + ": start date must be before or equal to end date");
            logger.log(Level.WARNING, "Validation failed: invalid date range");
        }
    }
    
    public static void validateNotNull(Object value, String fieldName, List<String> errors) {
        if (value == null) {
            errors.add(fieldName + ": cannot be null");
            logger.log(Level.WARNING, "Validation failed for {0}: cannot be null", fieldName);
        }
    }
    
    public static void validateNotEmpty(List<?> list, String fieldName, List<String> errors) {
        if (list == null || list.isEmpty()) {
            errors.add(fieldName + ": cannot be empty");
            logger.log(Level.WARNING, "Validation failed for {0}: cannot be empty", fieldName);
        }
    }
    
    public static void throwIfErrors(List<String> errors) throws InvalidDataException {
        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }
}

