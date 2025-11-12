package ua.library;

import ua.util.Utils;
import ua.enums.MembershipType;
import java.time.LocalDate;
import java.util.Objects;

public class Membership {
    
    private Reader reader;
    private LocalDate startDate;
    private LocalDate endDate;
    private MembershipType type;
    
    public Membership(Reader reader, LocalDate startDate, LocalDate endDate, MembershipType type) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader cannot be null");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Membership type cannot be null");
        }
        Utils.validateDateRange(startDate, endDate, "membership period");
        
        this.reader = reader;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }
    
    public static Membership of(Reader reader, LocalDate startDate, LocalDate endDate, MembershipType type) {
        return new Membership(reader, startDate, endDate, type);
    }
    
    public static Membership createYearlyFromNow(Reader reader, MembershipType type) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusYears(1);
        return new Membership(reader, start, end, type);
    }
    
    public Reader getReader() {
        return reader;
    }
    
    public void setReader(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader cannot be null");
        }
        this.reader = reader;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        Utils.validateDateRange(startDate, endDate, "membership period");
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        Utils.validateDateRange(startDate, endDate, "membership period");
        this.endDate = endDate;
    }
    
    public MembershipType getType() {
        return type;
    }
    
    public void setType(MembershipType type) {
        if (type == null) {
            throw new IllegalArgumentException("Membership type cannot be null");
        }
        this.type = type;
    }
    
    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }
    
    public boolean isExpired() {
        return LocalDate.now().isAfter(endDate);
    }
    
    public String getMembershipInfo() {
        return switch (type) {
            case STANDARD -> "Standard: " + type.getMaxBooks() + " books, " + type.getMaxLoanDays() + " days";
            case PREMIUM -> "Premium: " + type.getMaxBooks() + " books, " + type.getMaxLoanDays() + " days (VIP)";
            case STUDENT -> "Student: " + type.getMaxBooks() + " books, " + type.getMaxLoanDays() + " days (Discounted)";
            case SENIOR -> "Senior: " + type.getMaxBooks() + " books, " + type.getMaxLoanDays() + " days (Discounted)";
        };
    }
    
    @Override
    public String toString() {
        return "Membership{" +
                "reader=" + reader.readerId() +
                ", startDate=" + Utils.formatDate(startDate) +
                ", endDate=" + Utils.formatDate(endDate) +
                ", type=" + type +
                ", active=" + isActive() +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Membership that = (Membership) o;
        return Objects.equals(reader, that.reader) && 
               Objects.equals(startDate, that.startDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(reader, startDate);
    }
}
