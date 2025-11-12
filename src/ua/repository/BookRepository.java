package ua.repository;

import ua.library.Book;
import java.util.Comparator;
import java.util.List;
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
}

