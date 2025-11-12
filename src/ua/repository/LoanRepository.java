package ua.repository;

import ua.library.Loan;
import java.util.List;
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
}

