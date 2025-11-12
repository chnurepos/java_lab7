package ua.util;

import ua.library.*;
import ua.enums.*;
import ua.repository.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TestDataGenerator {
    
    private static final Logger logger = Logger.getLogger(TestDataGenerator.class.getName());
    private static final Random random = new Random();
    
    private static final String[] FIRST_NAMES = {"John", "Jane", "Bob", "Alice", "Charlie", "Diana", "Eve", "Frank"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis"};
    private static final String[] BOOK_TITLES = {"The Great Adventure", "Mystery of Time", "Science Fiction", 
                                                   "History of World", "Programming Guide", "Data Structures", 
                                                   "Algorithms", "Database Design", "Web Development", "Mobile Apps"};
    private static final String[] AUTHOR_FIRST_NAMES = {"George", "Isaac", "Terry", "Stephen", "J.K.", "Agatha"};
    private static final String[] AUTHOR_LAST_NAMES = {"Orwell", "Asimov", "Pratchett", "King", "Rowling", "Christie"};
    
    public static BookRepository generateBookRepository() {
        logger.log(Level.INFO, "Generating test BookRepository");
        BookRepository repo = new BookRepository();
        int count = ConfigLoader.getTestDataCount("books");
        
        AuthorRepository authorRepo = generateAuthorRepository();
        List<Author> authors = authorRepo.getAll();
        
        for (int i = 0; i < count; i++) {
            Author author = authors.get(random.nextInt(authors.size()));
            String title = BOOK_TITLES[i % BOOK_TITLES.length] + " " + (i + 1);
            String isbn = String.format("%013d", 1000000000000L + i);
            BookStatus status = BookStatus.values()[random.nextInt(BookStatus.values().length)];
            
            Book book = Book.of(title, author, isbn, status);
            repo.add(book);
        }
        
        logger.log(Level.INFO, "Generated {0} books", count);
        return repo;
    }
    
    public static ReaderRepository generateReaderRepository() {
        logger.log(Level.INFO, "Generating test ReaderRepository");
        ReaderRepository repo = new ReaderRepository();
        int count = ConfigLoader.getTestDataCount("readers");
        
        for (int i = 0; i < count; i++) {
            String firstName = FIRST_NAMES[i % FIRST_NAMES.length];
            String lastName = LAST_NAMES[i % LAST_NAMES.length];
            String readerId = "RD" + String.format("%05d", i + 1);
            
            Reader reader = Reader.of(firstName, lastName, readerId);
            repo.add(reader);
        }
        
        logger.log(Level.INFO, "Generated {0} readers", count);
        return repo;
    }
    
    public static AuthorRepository generateAuthorRepository() {
        logger.log(Level.INFO, "Generating test AuthorRepository");
        AuthorRepository repo = new AuthorRepository();
        int count = ConfigLoader.getTestDataCount("authors");
        
        for (int i = 0; i < count; i++) {
            String firstName = AUTHOR_FIRST_NAMES[i % AUTHOR_FIRST_NAMES.length];
            String lastName = AUTHOR_LAST_NAMES[i % AUTHOR_LAST_NAMES.length];
            int birthYear = 1900 + random.nextInt(100);
            
            Author author = Author.of(firstName, lastName, birthYear);
            repo.add(author);
        }
        
        logger.log(Level.INFO, "Generated {0} authors", count);
        return repo;
    }
    
    public static LoanRepository generateLoanRepository(BookRepository bookRepo, ReaderRepository readerRepo) {
        logger.log(Level.INFO, "Generating test LoanRepository");
        LoanRepository repo = new LoanRepository();
        int count = ConfigLoader.getTestDataCount("loans");
        
        List<Book> books = bookRepo.getAll();
        List<Reader> readers = readerRepo.getAll();
        
        if (books.isEmpty() || readers.isEmpty()) {
            logger.log(Level.WARNING, "Cannot generate loans: books or readers are empty");
            return repo;
        }
        
        for (int i = 0; i < count && i < books.size(); i++) {
            Book book = books.get(i % books.size());
            Reader reader = readers.get(i % readers.size());
            LocalDate issueDate = LocalDate.now().minusDays(random.nextInt(30));
            LocalDate returnDate = issueDate.plusDays(14 + random.nextInt(14));
            
            Loan loan = Loan.of(book, reader, issueDate, returnDate);
            repo.add(loan);
        }
        
        logger.log(Level.INFO, "Generated {0} loans", repo.size());
        return repo;
    }
    
    public static MembershipRepository generateMembershipRepository(ReaderRepository readerRepo) {
        logger.log(Level.INFO, "Generating test MembershipRepository");
        MembershipRepository repo = new MembershipRepository();
        int count = ConfigLoader.getTestDataCount("memberships");
        
        List<Reader> readers = readerRepo.getAll();
        if (readers.isEmpty()) {
            logger.log(Level.WARNING, "Cannot generate memberships: readers are empty");
            return repo;
        }
        
        MembershipType[] types = MembershipType.values();
        
        for (int i = 0; i < count && i < readers.size(); i++) {
            Reader reader = readers.get(i % readers.size());
            LocalDate startDate = LocalDate.now().minusMonths(random.nextInt(12));
            LocalDate endDate = startDate.plusYears(1);
            MembershipType type = types[random.nextInt(types.length)];
            
            Membership membership = Membership.of(reader, startDate, endDate, type);
            repo.add(membership);
        }
        
        logger.log(Level.INFO, "Generated {0} memberships", repo.size());
        return repo;
    }
}

