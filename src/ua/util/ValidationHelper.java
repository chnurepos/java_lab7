package ua.util;

import java.time.LocalDate;

class ValidationHelper {
    
    static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    static boolean isValidISBN(String isbn) {
        if (isbn == null) return false;
        String cleanISBN = isbn.replaceAll("[\\s-]", "");
        return cleanISBN.matches("\\d{10}|\\d{13}");
    }
    
    static boolean isValidYear(int year) {
        int currentYear = LocalDate.now().getYear();
        return year >= 1000 && year <= currentYear;
    }
    
    static boolean isValidReaderId(String readerId) {
        return isValidString(readerId) && readerId.matches("[A-Z0-9]{5,10}");
    }
    
    static boolean isDateRangeValid(LocalDate start, LocalDate end) {
        if (start == null || end == null) return false;
        return !start.isAfter(end);
    }
}
