package ua.library;

import ua.util.DataValidator;
import ua.util.InvalidDataException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public record Reader(String firstName, String lastName, String readerId) implements Comparable<Reader> {
    
    private static final Logger logger = Logger.getLogger(Reader.class.getName());
    
    public Reader {
        List<String> errors = new ArrayList<>();
        
        DataValidator.validateString(firstName, "firstName", errors);
        DataValidator.validateString(lastName, "lastName", errors);
        DataValidator.validateReaderId(readerId, errors);
        
        if (!errors.isEmpty()) {
            logger.log(Level.SEVERE, "Failed to create Reader: {0}", errors);
            throw new InvalidDataException(errors);
        }
        
        logger.log(Level.INFO, "Reader created successfully: {0} ({1})", new Object[]{firstName + " " + lastName, readerId});
    }
    
    public static Reader of(String firstName, String lastName, String readerId) throws InvalidDataException {
        return new Reader(firstName, lastName, readerId);
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public int compareTo(Reader other) {
        return this.readerId.compareTo(other.readerId);
    }
    
    public static Comparator<Reader> byFirstName() {
        return Comparator.comparing(Reader::firstName, String.CASE_INSENSITIVE_ORDER);
    }
    
    public static Comparator<Reader> byLastName() {
        return Comparator.comparing(Reader::lastName, String.CASE_INSENSITIVE_ORDER);
    }
    
    public static Comparator<Reader> byFullName() {
        return Comparator.comparing(Reader::getFullName, String.CASE_INSENSITIVE_ORDER);
    }
}
