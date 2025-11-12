package ua.repository;

import ua.library.Author;
import java.util.*;
import java.util.stream.Collectors;
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
    
    public List<Author> findByLastName(String lastName) {
        logger.log(Level.INFO, "Searching authors by last name: {0}", lastName);
        return getAll().stream()
                .filter(author -> author.lastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }
    
    public List<Author> findByFirstName(String firstName) {
        logger.log(Level.INFO, "Searching authors by first name: {0}", firstName);
        return getAll().stream()
                .filter(author -> author.firstName().equalsIgnoreCase(firstName))
                .collect(Collectors.toList());
    }
    
    public List<Author> findByBirthYearRange(int minYear, int maxYear) {
        logger.log(Level.INFO, "Searching authors by birth year range: {0} - {1}", new Object[]{minYear, maxYear});
        return getAll().stream()
                .filter(author -> author.birthYear() >= minYear && author.birthYear() <= maxYear)
                .collect(Collectors.toList());
    }
    
    public List<Author> findByBirthYear(int birthYear) {
        logger.log(Level.INFO, "Searching authors by birth year: {0}", birthYear);
        return getAll().stream()
                .filter(author -> author.birthYear() == birthYear)
                .collect(Collectors.toList());
    }
    
    public Optional<Author> findOldest() {
        logger.log(Level.INFO, "Finding oldest author");
        return getAll().stream()
                .min(Author.byBirthYear());
    }
    
    public Optional<Author> findYoungest() {
        logger.log(Level.INFO, "Finding youngest author");
        return getAll().stream()
                .max(Author.byBirthYear());
    }
    
    public List<String> getAllLastNames() {
        logger.log(Level.INFO, "Getting all last names");
        return getAll().stream()
                .map(Author::lastName)
                .distinct()
                .collect(Collectors.toList());
    }
    
    public Map<Integer, Long> countByBirthYear() {
        logger.log(Level.INFO, "Counting authors by birth year");
        return getAll().stream()
                .collect(Collectors.groupingBy(Author::birthYear, Collectors.counting()));
    }
    
    public double getAverageBirthYear() {
        logger.log(Level.INFO, "Calculating average birth year");
        return getAll().stream()
                .mapToInt(Author::birthYear)
                .average()
                .orElse(0.0);
    }
}

