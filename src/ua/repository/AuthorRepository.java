package ua.repository;

import ua.library.Author;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AuthorRepository extends GenericRepository<Author> {
    
    private static final Logger logger = Logger.getLogger(AuthorRepository.class.getName());
    
    public AuthorRepository() {
        super(author -> author.firstName() + "|" + author.lastName() + "|" + author.birthYear());
        logger.log(Level.INFO, "AuthorRepository initialized");
    }
    
    public List<Author> sortByName() {
        logger.log(Level.INFO, "Sorting authors by name");
        List<Author> sorted = getAll();
        sorted.sort(Author::compareTo);
        return sorted;
    }
    
    public List<Author> sortByBirthYear() {
        logger.log(Level.INFO, "Sorting authors by birth year");
        List<Author> sorted = getAll();
        sorted.sort(Author.byBirthYear());
        return sorted;
    }
    
    public List<Author> sortByFirstName() {
        logger.log(Level.INFO, "Sorting authors by first name");
        List<Author> sorted = getAll();
        sorted.sort(Author.byFirstName());
        return sorted;
    }
    
    public List<Author> sortByBirthYearDescending() {
        logger.log(Level.INFO, "Sorting authors by birth year descending");
        List<Author> sorted = getAll();
        sorted.sort(Author.byBirthYearDescending());
        return sorted;
    }
}

