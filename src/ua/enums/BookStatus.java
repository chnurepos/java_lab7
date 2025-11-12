package ua.enums;

public enum BookStatus {
    AVAILABLE,
    CHECKED_OUT,
    RESERVED,
    LOST;
    
    public String getDescription() {
        return switch (this) {
            case AVAILABLE -> "Book is available for checkout";
            case CHECKED_OUT -> "Book is currently checked out";
            case RESERVED -> "Book is reserved for a patron";
            case LOST -> "Book has been reported lost";
        };
    }
    
    public boolean isAvailableForCheckout() {
        return switch (this) {
            case AVAILABLE -> true;
            case CHECKED_OUT, RESERVED, LOST -> false;
        };
    }
}
