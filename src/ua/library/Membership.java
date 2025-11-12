package ua.library;

import ua.util.DataValidator;
import ua.util.InvalidDataException;
import ua.enums.MembershipType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Membership implements Comparable<Membership> {
    
    private static final Logger logger = Logger.getLogger(Membership.class.getName());
    
    private Reader reader;
    private LocalDate startDate;
    private LocalDate endDate;
    private MembershipType type;
    
    public Membership(Reader reader, LocalDate startDate, LocalDate endDate, MembershipType type) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        
        DataValidator.validateNotNull(reader, "reader", errors);
        DataValidator.validateNotNull(startDate, "startDate", errors);
        DataValidator.validateNotNull(endDate, "endDate", errors);
        DataValidator.validateNotNull(type, "type", errors);
        DataValidator.validateDateRange(startDate, endDate, "membership period", errors);
        
        if (!errors.isEmpty()) {
            logger.log(Level.SEVERE, "Failed to create Membership: {0}", errors);
            throw new InvalidDataException(errors);
        }
        
        this.reader = reader;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        logger.log(Level.INFO, "Membership created successfully: {0} - {1}", new Object[]{reader.readerId(), type});
    }
    
    public static Membership of(Reader reader, LocalDate startDate, LocalDate endDate, MembershipType type) throws InvalidDataException {
        return new Membership(reader, startDate, endDate, type);
    }
    
    public static Membership createYearlyFromNow(Reader reader, MembershipType type) throws InvalidDataException {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusYears(1);
        return new Membership(reader, start, end, type);
    }
    
    public Reader getReader() {
        return reader;
    }
    
    public void setReader(Reader reader) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        DataValidator.validateNotNull(reader, "reader", errors);
        DataValidator.throwIfErrors(errors);
        
        logger.log(Level.INFO, "Membership reader updated");
        this.reader = reader;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        DataValidator.validateNotNull(startDate, "startDate", errors);
        if (endDate != null) {
            DataValidator.validateDateRange(startDate, endDate, "membership period", errors);
        }
        DataValidator.throwIfErrors(errors);
        
        logger.log(Level.INFO, "Membership start date updated");
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        DataValidator.validateNotNull(endDate, "endDate", errors);
        if (startDate != null) {
            DataValidator.validateDateRange(startDate, endDate, "membership period", errors);
        }
        DataValidator.throwIfErrors(errors);
        
        logger.log(Level.INFO, "Membership end date updated");
        this.endDate = endDate;
    }
    
    public MembershipType getType() {
        return type;
    }
    
    public void setType(MembershipType type) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        DataValidator.validateNotNull(type, "type", errors);
        DataValidator.throwIfErrors(errors);
        
        logger.log(Level.INFO, "Membership type updated: {0}", type);
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
    
    @Override
    public int compareTo(Membership other) {
        return this.startDate.compareTo(other.startDate);
    }
    
    public static Comparator<Membership> byEndDate() {
        return Comparator.comparing(Membership::getEndDate);
    }
    
    public static Comparator<Membership> byType() {
        return Comparator.comparing(Membership::getType);
    }
    
    public static Comparator<Membership> byReader() {
        return Comparator.comparing(Membership::getReader);
    }
    
    public static Comparator<Membership> byStartDateDescending() {
        return Comparator.comparing(Membership::getStartDate).reversed();
    }
}
