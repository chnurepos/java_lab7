package ua.library;

import ua.util.DataValidator;
import ua.util.InvalidDataException;
import ua.enums.BookStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Book implements Comparable<Book> {
    
    private static final Logger logger = Logger.getLogger(Book.class.getName());
    
    private String title;
    private List<Author> authors;
    private String isbn;
    private BookStatus status;
    
    public Book(String title, List<Author> authors, String isbn, BookStatus status) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        
        DataValidator.validateString(title, "title", errors);
        DataValidator.validateISBN(isbn, errors);
        DataValidator.validateNotEmpty(authors, "authors", errors);
        DataValidator.validateNotNull(status, "status", errors);
        
        if (!errors.isEmpty()) {
            logger.log(Level.SEVERE, "Failed to create Book: {0}", errors);
            throw new InvalidDataException(errors);
        }
        
        this.title = title;
        this.authors = new ArrayList<>(authors);
        this.isbn = isbn;
        this.status = status;
        logger.log(Level.INFO, "Book created successfully: {0}", title);
    }
    
    public static Book of(String title, Author author, String isbn, BookStatus status) throws InvalidDataException {
        return new Book(title, List.of(author), isbn, status);
    }
    
    public static Book of(String title, List<Author> authors, String isbn, BookStatus status) throws InvalidDataException {
        return new Book(title, authors, isbn, status);
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        DataValidator.validateString(title, "title", errors);
        DataValidator.throwIfErrors(errors);
        
        logger.log(Level.INFO, "Book title updated: {0}", title);
        this.title = title;
    }
    
    public List<Author> getAuthors() {
        return Collections.unmodifiableList(authors);
    }
    
    public void setAuthors(List<Author> authors) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        DataValidator.validateNotEmpty(authors, "authors", errors);
        DataValidator.throwIfErrors(errors);
        
        logger.log(Level.INFO, "Book authors updated");
        this.authors = new ArrayList<>(authors);
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        DataValidator.validateISBN(isbn, errors);
        DataValidator.throwIfErrors(errors);
        
        logger.log(Level.INFO, "Book ISBN updated: {0}", isbn);
        this.isbn = isbn;
    }
    
    public BookStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookStatus status) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        DataValidator.validateNotNull(status, "status", errors);
        DataValidator.throwIfErrors(errors);
        
        logger.log(Level.INFO, "Book status updated: {0}", status);
        this.status = status;
    }
    
    public String getStatusInfo() {
        return switch (status) {
            case AVAILABLE -> "Available - Ready for checkout";
            case CHECKED_OUT -> "Checked Out - Currently borrowed";
            case RESERVED -> "Reserved - Waiting for pickup";
            case LOST -> "Lost - Not available";
        };
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                ", isbn='" + isbn + '\'' +
                ", status=" + status +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
    
    @Override
    public int compareTo(Book other) {
        return this.title.compareToIgnoreCase(other.title);
    }
    
    public static Comparator<Book> byIsbn() {
        return Comparator.comparing(Book::getIsbn);
    }
    
    public static Comparator<Book> byStatus() {
        return Comparator.comparing(Book::getStatus);
    }
    
    public static Comparator<Book> byFirstAuthor() {
        return Comparator.comparing(book -> book.getAuthors().get(0), Author::compareTo);
    }
    
    public static Comparator<Book> byTitleDescending() {
        return Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER).reversed();
    }
}
