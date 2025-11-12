package ua.repository;

import ua.library.Loan;
import ua.library.Reader;
import ua.library.Book;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;

public class LoanRepository extends GenericRepository<Loan> {
    
    private static final Logger logger = Logger.getLogger(LoanRepository.class.getName());
    
    public LoanRepository() {
        super(loan -> loan.getBook().getIsbn() + "|" + 
                    loan.getReader().readerId() + "|" + 
                    loan.getIssueDate().toString());
        logger.log(Level.INFO, "LoanRepository initialized");
    }
    
    public List<Loan> sortByIssueDate() {
        logger.log(Level.INFO, "Sorting loans by issue date");
        List<Loan> sorted = getAll();
        sorted.sort(Loan::compareTo);
        return sorted;
    }
    
    public List<Loan> sortByReturnDate() {
        logger.log(Level.INFO, "Sorting loans by return date");
        List<Loan> sorted = getAll();
        sorted.sort(Loan.byReturnDate());
        return sorted;
    }
    
    public List<Loan> sortByBookTitle() {
        logger.log(Level.INFO, "Sorting loans by book title");
        List<Loan> sorted = getAll();
        sorted.sort(Loan.byBookTitle());
        return sorted;
    }
    
    public List<Loan> sortByReader() {
        logger.log(Level.INFO, "Sorting loans by reader");
        List<Loan> sorted = getAll();
        sorted.sort(Loan.byReader());
        return sorted;
    }
    
    public List<Loan> sortByIssueDateDescending() {
        logger.log(Level.INFO, "Sorting loans by issue date descending");
        List<Loan> sorted = getAll();
        sorted.sort(Loan.byIssueDateDescending());
        return sorted;
    }
    
    public List<Loan> findByReader(Reader reader) {
        logger.log(Level.INFO, "Searching loans by reader: {0}", reader.readerId());
        return getAll().stream()
                .filter(loan -> loan.getReader().equals(reader))
                .collect(Collectors.toList());
    }
    
    public List<Loan> findByBook(Book book) {
        logger.log(Level.INFO, "Searching loans by book: {0}", book.getTitle());
        return getAll().stream()
                .filter(loan -> loan.getBook().equals(book))
                .collect(Collectors.toList());
    }
    
    public List<Loan> findByIssueDateRange(LocalDate startDate, LocalDate endDate) {
        logger.log(Level.INFO, "Searching loans by issue date range: {0} - {1}", new Object[]{startDate, endDate});
        return getAll().stream()
                .filter(loan -> !loan.getIssueDate().isBefore(startDate) && !loan.getIssueDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
    
    public List<Loan> findOverdue() {
        logger.log(Level.INFO, "Searching overdue loans");
        return getAll().stream()
                .filter(Loan::isOverdue)
                .collect(Collectors.toList());
    }
    
    public List<Loan> findByReturnDateRange(LocalDate startDate, LocalDate endDate) {
        logger.log(Level.INFO, "Searching loans by return date range: {0} - {1}", new Object[]{startDate, endDate});
        return getAll().stream()
                .filter(loan -> loan.getReturnDate() != null && 
                        !loan.getReturnDate().isBefore(startDate) && 
                        !loan.getReturnDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
    
    public List<String> getAllBookTitles() {
        logger.log(Level.INFO, "Getting all book titles from loans");
        return getAll().stream()
                .map(loan -> loan.getBook().getTitle())
                .distinct()
                .collect(Collectors.toList());
    }
    
    public Map<Reader, Long> countByReader() {
        logger.log(Level.INFO, "Counting loans by reader");
        return getAll().stream()
                .collect(Collectors.groupingBy(Loan::getReader, Collectors.counting()));
    }
    
    public long countOverdue() {
        logger.log(Level.INFO, "Counting overdue loans");
        return getAll().stream()
                .filter(Loan::isOverdue)
                .count();
    }
    
    public Optional<Loan> findOldestLoan() {
        logger.log(Level.INFO, "Finding oldest loan");
        return getAll().stream()
                .min(Loan::compareTo);
    }
}

