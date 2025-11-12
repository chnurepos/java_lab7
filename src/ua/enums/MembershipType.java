package ua.enums;

public enum MembershipType {
    STANDARD(14, 3),
    PREMIUM(30, 10),
    STUDENT(21, 5),
    SENIOR(21, 5);
    
    private final int maxLoanDays;
    private final int maxBooks;
    
    MembershipType(int maxLoanDays, int maxBooks) {
        this.maxLoanDays = maxLoanDays;
        this.maxBooks = maxBooks;
    }
    
    public int getMaxLoanDays() {
        return maxLoanDays;
    }
    
    public int getMaxBooks() {
        return maxBooks;
    }
    
    public double getMonthlyFee() {
        return switch (this) {
            case STANDARD -> 10.0;
            case PREMIUM -> 25.0;
            case STUDENT -> 5.0;
            case SENIOR -> 5.0;
        };
    }
    
    public String getDescription() {
        return switch (this) {
            case STANDARD -> "Standard membership with basic benefits";
            case PREMIUM -> "Premium membership with extended benefits";
            case STUDENT -> "Discounted membership for students";
            case SENIOR -> "Discounted membership for seniors";
        };
    }
}
