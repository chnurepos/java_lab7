package ua.library;

import ua.util.Utils;

public record Reader(String firstName, String lastName, String readerId) {
    
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
}
