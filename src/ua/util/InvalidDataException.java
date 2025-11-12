package ua.util;

import java.util.ArrayList;
import java.util.List;

public class InvalidDataException extends Exception {
    
    private final List<String> errors;
    
    public InvalidDataException(String message) {
        super(message);
        this.errors = new ArrayList<>();
        this.errors.add(message);
    }
    
    public InvalidDataException(List<String> errors) {
        super(formatErrors(errors));
        this.errors = new ArrayList<>(errors);
    }
    
    public InvalidDataException(String field, String error) {
        super(field + ": " + error);
        this.errors = new ArrayList<>();
        this.errors.add(field + ": " + error);
    }
    
    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
    
    public void addError(String field, String error) {
        errors.add(field + ": " + error);
    }
    
    public void addError(String error) {
        errors.add(error);
    }
    
    @Override
    public String getMessage() {
        return formatErrors(errors);
    }
    
    private static String formatErrors(List<String> errors) {
        if (errors == null || errors.isEmpty()) {
            return "Validation failed";
        }
        return String.join("; ", errors);
    }
}

