package ua.repository;

import ua.library.Book;
import ua.library.Author;
import ua.enums.BookStatus;
import java.util.*;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;

public class BookRepository extends GenericRepository<Book> {
    
    private static final Logger logger = Logger.getLogger(BookRepository.class.getName());
    
    public BookRepository() {
        super(Book::getIsbn);
        logger.log(Level.INFO, "BookRepository initialized");
    }
    
    public List<Book> sortByTitle() {
        logger.log(Level.INFO, "Sorting books by title");
        List<Book> sorted = getAll();
        sorted.sort(Book::compareTo);
        return sorted;
    }
    
    public List<Book> sortByIsbn() {
        logger.log(Level.INFO, "Sorting books by ISBN");
        List<Book> sorted = getAll();
        sorted.sort(Book.byIsbn());
        return sorted;
    }
    
    public List<Book> sortByStatus() {
        logger.log(Level.INFO, "Sorting books by status");
        List<Book> sorted = getAll();
        sorted.sort(Book.byStatus());
        return sorted;
    }
    
    public List<Book> sortByFirstAuthor() {
        logger.log(Level.INFO, "Sorting books by first author");
        List<Book> sorted = getAll();
        sorted.sort(Book.byFirstAuthor());
        return sorted;
    }
    
    public List<Book> sortByTitleDescending() {
        logger.log(Level.INFO, "Sorting books by title descending");
        List<Book> sorted = getAll();
        sorted.sort(Book.byTitleDescending());
        return sorted;
    }
    
    public List<Book> findByTitle(String title) {
        logger.log(Level.INFO, "Searching books by title: {0}", title);
        return getAll().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
    }
    
    public List<Book> findByTitleContains(String keyword) {
        logger.log(Level.INFO, "Searching books by title containing: {0}", keyword);
        return getAll().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Book> findByStatus(BookStatus status) {
        logger.log(Level.INFO, "Searching books by status: {0}", status);
        return getAll().stream()
                .filter(book -> book.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    public List<Book> findByAuthor(Author author) {
        logger.log(Level.INFO, "Searching books by author: {0}", author.getFullName());
        return getAll().stream()
                .filter(book -> book.getAuthors().contains(author))
                .collect(Collectors.toList());
    }
    
    public List<Book> findByAuthorName(String authorName) {
        logger.log(Level.INFO, "Searching books by author name: {0}", authorName);
        return getAll().stream()
                .filter(book -> book.getAuthors().stream()
                        .anyMatch(author -> author.getFullName().toLowerCase().contains(authorName.toLowerCase())))
                .collect(Collectors.toList());
    }
    
    public List<String> getAllTitles() {
        logger.log(Level.INFO, "Getting all book titles");
        return getAll().stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }
    
    public Set<Author> getAllAuthors() {
        logger.log(Level.INFO, "Getting all authors");
        return getAll().stream()
                .flatMap(book -> book.getAuthors().stream())
                .collect(Collectors.toSet());
    }
    
    public Map<BookStatus, Long> countByStatus() {
        logger.log(Level.INFO, "Counting books by status");
        return getAll().stream()
                .collect(Collectors.groupingBy(Book::getStatus, Collectors.counting()));
    }
    
    public Optional<Book> findFirstByStatus(BookStatus status) {
        logger.log(Level.INFO, "Finding first book by status: {0}", status);
        return getAll().stream()
                .filter(book -> book.getStatus() == status)
                .findFirst();
    }
    
    public long countByTitleContains(String keyword) {
        logger.log(Level.INFO, "Counting books with title containing: {0}", keyword);
        return getAll().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .count();
    }
}

