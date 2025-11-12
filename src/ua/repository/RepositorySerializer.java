package ua.repository;

import ua.library.*;
import ua.util.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class RepositorySerializer {
    
    private static final Logger logger = Logger.getLogger(RepositorySerializer.class.getName());
    
    public static void saveBookRepository(BookRepository repo) throws DataSerializationException {
        String jsonPath = ConfigLoader.getJsonPath("books");
        String yamlPath = ConfigLoader.getYamlPath("books");
        
        List<Book> books = repo.getAll();
        DataSerializer.saveToJson(books, jsonPath, Book.class);
        DataSerializer.saveToYaml(books, yamlPath, Book.class);
        
        logger.log(Level.INFO, "BookRepository saved to JSON and YAML");
    }
    
    public static BookRepository loadBookRepository() throws DataSerializationException {
        String jsonPath = ConfigLoader.getJsonPath("books");
        BookRepository repo = new BookRepository();
        
        List<Book> books = DataSerializer.loadFromJson(jsonPath, Book.class);
        books.forEach(repo::add);
        
        logger.log(Level.INFO, "BookRepository loaded from JSON with {0} books", books.size());
        return repo;
    }
    
    public static BookRepository loadBookRepositoryFromYaml() throws DataSerializationException {
        String yamlPath = ConfigLoader.getYamlPath("books");
        BookRepository repo = new BookRepository();
        
        List<Book> books = DataSerializer.loadFromYaml(yamlPath, Book.class);
        books.forEach(repo::add);
        
        logger.log(Level.INFO, "BookRepository loaded from YAML with {0} books", books.size());
        return repo;
    }
    
    public static void saveReaderRepository(ReaderRepository repo) throws DataSerializationException {
        String jsonPath = ConfigLoader.getJsonPath("readers");
        String yamlPath = ConfigLoader.getYamlPath("readers");
        
        List<Reader> readers = repo.getAll();
        DataSerializer.saveToJson(readers, jsonPath, Reader.class);
        DataSerializer.saveToYaml(readers, yamlPath, Reader.class);
        
        logger.log(Level.INFO, "ReaderRepository saved to JSON and YAML");
    }
    
    public static ReaderRepository loadReaderRepository() throws DataSerializationException {
        String jsonPath = ConfigLoader.getJsonPath("readers");
        ReaderRepository repo = new ReaderRepository();
        
        List<Reader> readers = DataSerializer.loadFromJson(jsonPath, Reader.class);
        readers.forEach(repo::add);
        
        logger.log(Level.INFO, "ReaderRepository loaded from JSON with {0} readers", readers.size());
        return repo;
    }
    
    public static void saveAuthorRepository(AuthorRepository repo) throws DataSerializationException {
        String jsonPath = ConfigLoader.getJsonPath("authors");
        String yamlPath = ConfigLoader.getYamlPath("authors");
        
        List<Author> authors = repo.getAll();
        DataSerializer.saveToJson(authors, jsonPath, Author.class);
        DataSerializer.saveToYaml(authors, yamlPath, Author.class);
        
        logger.log(Level.INFO, "AuthorRepository saved to JSON and YAML");
    }
    
    public static AuthorRepository loadAuthorRepository() throws DataSerializationException {
        String jsonPath = ConfigLoader.getJsonPath("authors");
        AuthorRepository repo = new AuthorRepository();
        
        List<Author> authors = DataSerializer.loadFromJson(jsonPath, Author.class);
        authors.forEach(repo::add);
        
        logger.log(Level.INFO, "AuthorRepository loaded from JSON with {0} authors", authors.size());
        return repo;
    }
    
    public static void saveLoanRepository(LoanRepository repo) throws DataSerializationException {
        String jsonPath = ConfigLoader.getJsonPath("loans");
        String yamlPath = ConfigLoader.getYamlPath("loans");
        
        List<Loan> loans = repo.getAll();
        DataSerializer.saveToJson(loans, jsonPath, Loan.class);
        DataSerializer.saveToYaml(loans, yamlPath, Loan.class);
        
        logger.log(Level.INFO, "LoanRepository saved to JSON and YAML");
    }
    
    public static LoanRepository loadLoanRepository() throws DataSerializationException {
        String jsonPath = ConfigLoader.getJsonPath("loans");
        LoanRepository repo = new LoanRepository();
        
        List<Loan> loans = DataSerializer.loadFromJson(jsonPath, Loan.class);
        loans.forEach(repo::add);
        
        logger.log(Level.INFO, "LoanRepository loaded from JSON with {0} loans", loans.size());
        return repo;
    }
    
    public static void saveMembershipRepository(MembershipRepository repo) throws DataSerializationException {
        String jsonPath = ConfigLoader.getJsonPath("memberships");
        String yamlPath = ConfigLoader.getYamlPath("memberships");
        
        List<Membership> memberships = repo.getAll();
        DataSerializer.saveToJson(memberships, jsonPath, Membership.class);
        DataSerializer.saveToYaml(memberships, yamlPath, Membership.class);
        
        logger.log(Level.INFO, "MembershipRepository saved to JSON and YAML");
    }
    
    public static MembershipRepository loadMembershipRepository() throws DataSerializationException {
        String jsonPath = ConfigLoader.getJsonPath("memberships");
        MembershipRepository repo = new MembershipRepository();
        
        List<Membership> memberships = DataSerializer.loadFromJson(jsonPath, Membership.class);
        memberships.forEach(repo::add);
        
        logger.log(Level.INFO, "MembershipRepository loaded from JSON with {0} memberships", memberships.size());
        return repo;
    }
}

