package ua.library;

import ua.util.Utils;
import java.util.Comparator;

public record Reader(String firstName, String lastName, String readerId) implements Comparable<Reader> {
    
    public Reader {
        Utils.validateString(firstName, "First name");
        Utils.validateString(lastName, "Last name");
        Utils.validateReaderId(readerId);
    }
    
    public static Reader of(String firstName, String lastName, String readerId) {
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
