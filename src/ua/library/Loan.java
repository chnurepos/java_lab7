package ua.library;

import ua.util.Utils;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

public class Loan implements Comparable<Loan> {
    
    private Book book;
    private Reader reader;
    private LocalDate issueDate;
    private LocalDate returnDate;
    
    public Loan(Book book, Reader reader, LocalDate issueDate, LocalDate returnDate) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (reader == null) {
            throw new IllegalArgumentException("Reader cannot be null");
        }
        if (issueDate == null) {
            throw new IllegalArgumentException("Issue date cannot be null");
        }
        Utils.validateDateRange(issueDate, returnDate, "loan period");
        
        this.book = book;
        this.reader = reader;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }
    
    public static Loan of(Book book, Reader reader, LocalDate issueDate, LocalDate returnDate) {
        return new Loan(book, reader, issueDate, returnDate);
    }
    
    public static Loan issueNow(Book book, Reader reader, LocalDate returnDate) {
        return new Loan(book, reader, LocalDate.now(), returnDate);
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        this.book = book;
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
    
    public LocalDate getIssueDate() {
        return issueDate;
    }
    
    public void setIssueDate(LocalDate issueDate) {
        if (issueDate == null) {
            throw new IllegalArgumentException("Issue date cannot be null");
        }
        Utils.validateDateRange(issueDate, returnDate, "loan period");
        this.issueDate = issueDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        Utils.validateDateRange(issueDate, returnDate, "loan period");
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
