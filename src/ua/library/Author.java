package ua.library;

import ua.util.DataValidator;
import ua.util.InvalidDataException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public record Author(String firstName, String lastName, int birthYear) implements Comparable<Author> {
    
    private static final Logger logger = Logger.getLogger(Author.class.getName());
    
    public Author {
        List<String> errors = new ArrayList<>();
        
        DataValidator.validateString(firstName, "firstName", errors);
        DataValidator.validateString(lastName, "lastName", errors);
        DataValidator.validateYear(birthYear, "birthYear", errors);
        
        if (!errors.isEmpty()) {
            logger.log(Level.SEVERE, "Failed to create Author: {0}", errors);
            throw new InvalidDataException(errors);
        }
        
        logger.log(Level.INFO, "Author created successfully: {0} {1}", new Object[]{firstName, lastName});
    }
    
    public static Author of(String firstName, String lastName, int birthYear) throws InvalidDataException {
        return new Author(firstName, lastName, birthYear);
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public int compareTo(Author other) {
        int lastNameCompare = this.lastName.compareToIgnoreCase(other.lastName);
        if (lastNameCompare != 0) {
            return lastNameCompare;
        }
        return this.firstName.compareToIgnoreCase(other.firstName);
    }
    
    public static Comparator<Author> byBirthYear() {
        return Comparator.comparingInt(Author::birthYear);
    }
    
    public static Comparator<Author> byFirstName() {
        return Comparator.comparing(Author::firstName, String.CASE_INSENSITIVE_ORDER);
    }
    
    public static Comparator<Author> byBirthYearDescending() {
        return byBirthYear().reversed();
    }
}
