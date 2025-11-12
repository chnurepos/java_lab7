package ua.library;

import ua.util.Utils;
import java.util.Comparator;

public record Author(String firstName, String lastName, int birthYear) implements Comparable<Author> {
    
    public Author {
        Utils.validateString(firstName, "First name");
        Utils.validateString(lastName, "Last name");
        Utils.validateYear(birthYear, "Birth year");
    }
    
    public static Author of(String firstName, String lastName, int birthYear) {
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
