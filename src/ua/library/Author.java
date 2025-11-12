package ua.library;

import ua.util.Utils;

public record Author(String firstName, String lastName, int birthYear) {
    
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
}
