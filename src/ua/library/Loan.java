package ua.library;

import ua.util.DataValidator;
import ua.util.InvalidDataException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Loan implements Comparable<Loan> {
    
    private static final Logger logger = Logger.getLogger(Loan.class.getName());
    
    private Book book;
    private Reader reader;
    private LocalDate issueDate;
    private LocalDate returnDate;
    
    public Loan(Book book, Reader reader, LocalDate issueDate, LocalDate returnDate) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        
        DataValidator.validateNotNull(book, "book", errors);
        DataValidator.validateNotNull(reader, "reader", errors);
        DataValidator.validateNotNull(issueDate, "issueDate", errors);
        DataValidator.validateDateRange(issueDate, returnDate, "loan period", errors);
        
        if (!errors.isEmpty()) {
            logger.log(Level.SEVERE, "Failed to create Loan: {0}", errors);
            throw new InvalidDataException(errors);
        }
        
        this.book = book;
        this.reader = reader;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        logger.log(Level.INFO, "Loan created successfully: {0} - {1}", new Object[]{book.getTitle(), reader.readerId()});
    }
    
    public static Loan of(Book book, Reader reader, LocalDate issueDate, LocalDate returnDate) throws InvalidDataException {
        return new Loan(book, reader, issueDate, returnDate);
    }
    
    public static Loan issueNow(Book book, Reader reader, LocalDate returnDate) throws InvalidDataException {
        return new Loan(book, reader, LocalDate.now(), returnDate);
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        DataValidator.validateNotNull(book, "book", errors);
        DataValidator.throwIfErrors(errors);
        
        logger.log(Level.INFO, "Loan book updated");
        this.book = book;
    }
    
    public Reader getReader() {
        return reader;
    }
    
    public void setReader(Reader reader) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        DataValidator.validateNotNull(reader, "reader", errors);
        DataValidator.throwIfErrors(errors);
        
        logger.log(Level.INFO, "Loan reader updated");
        this.reader = reader;
    }
    
    public LocalDate getIssueDate() {
        return issueDate;
    }
    
    public void setIssueDate(LocalDate issueDate) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        DataValidator.validateNotNull(issueDate, "issueDate", errors);
        if (returnDate != null) {
            DataValidator.validateDateRange(issueDate, returnDate, "loan period", errors);
        }
        DataValidator.throwIfErrors(errors);
        
        logger.log(Level.INFO, "Loan issue date updated");
        this.issueDate = issueDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        if (issueDate != null) {
            DataValidator.validateDateRange(issueDate, returnDate, "loan period", errors);
        }
        DataValidator.throwIfErrors(errors);
        
        logger.log(Level.INFO, "Loan return date updated");
        this.returnDate = returnDate;
    }
    
    public boolean isOverdue() {
        return returnDate != null && LocalDate.now().isAfter(returnDate);
    }
    
    @Override
    public String toString() {
        return "Loan{" +
                "book=" + book.getTitle() +
                ", reader=" + reader.readerId() +
                ", issueDate=" + Utils.formatDate(issueDate) +
                ", returnDate=" + Utils.formatDate(returnDate) +
                ", overdue=" + isOverdue() +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(book, loan.book) && 
               Objects.equals(reader, loan.reader) && 
               Objects.equals(issueDate, loan.issueDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(book, reader, issueDate);
    }
    
    @Override
    public int compareTo(Loan other) {
        return this.issueDate.compareTo(other.issueDate);
    }
    
    public static Comparator<Loan> byReturnDate() {
        return Comparator.comparing(Loan::getReturnDate, Comparator.nullsLast(Comparator.naturalOrder()));
    }
    
    public static Comparator<Loan> byBookTitle() {
        return Comparator.comparing(loan -> loan.getBook().getTitle(), String.CASE_INSENSITIVE_ORDER);
    }
    
    public static Comparator<Loan> byReader() {
        return Comparator.comparing(Loan::getReader);
    }
    
    public static Comparator<Loan> byIssueDateDescending() {
        return Comparator.comparing(Loan::getIssueDate).reversed();
    }
}
