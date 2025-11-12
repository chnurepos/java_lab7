package ua.library;

import ua.util.Utils;
import ua.enums.BookStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Book implements Comparable<Book> {
    
    private String title;
    private List<Author> authors;
    private String isbn;
    private BookStatus status;
    
    public Book(String title, List<Author> authors, String isbn, BookStatus status) {
        Utils.validateString(title, "Title");
        Utils.validateISBN(isbn);
        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Book must have at least one author");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.title = title;
        this.authors = new ArrayList<>(authors);
        this.isbn = isbn;
        this.status = status;
    }
    
    public static Book of(String title, Author author, String isbn, BookStatus status) {
        return new Book(title, List.of(author), isbn, status);
    }
    
    public static Book of(String title, List<Author> authors, String isbn, BookStatus status) {
        return new Book(title, authors, isbn, status);
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        Utils.validateString(title, "Title");
        this.title = title;
    }
    
    public List<Author> getAuthors() {
        return Collections.unmodifiableList(authors);
    }
    
    public void setAuthors(List<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Book must have at least one author");
        }
        this.authors = new ArrayList<>(authors);
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        Utils.validateISBN(isbn);
        this.isbn = isbn;
    }
    
    public BookStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }
    
    public String getStatusInfo() {
        return switch (status) {
            case AVAILABLE -> "✓ Available - Ready for checkout";
            case CHECKED_OUT -> "✗ Checked Out - Currently borrowed";
            case RESERVED -> "⊙ Reserved - Waiting for pickup";
            case LOST -> "⚠ Lost - Not available";
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
