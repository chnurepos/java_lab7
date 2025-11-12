package ua.repository;

import ua.library.Reader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ReaderRepository extends GenericRepository<Reader> {
    
    private static final Logger logger = Logger.getLogger(ReaderRepository.class.getName());
    
    public ReaderRepository() {
        super(Reader::readerId);
        logger.log(Level.INFO, "ReaderRepository initialized");
    }
    
    public List<Reader> sortByReaderId() {
        logger.log(Level.INFO, "Sorting readers by reader ID");
        List<Reader> sorted = getAll();
        sorted.sort(Reader::compareTo);
        return sorted;
    }
    
    public List<Reader> sortByFirstName() {
        logger.log(Level.INFO, "Sorting readers by first name");
        List<Reader> sorted = getAll();
        sorted.sort(Reader.byFirstName());
        return sorted;
    }
    
    public List<Reader> sortByLastName() {
        logger.log(Level.INFO, "Sorting readers by last name");
        List<Reader> sorted = getAll();
        sorted.sort(Reader.byLastName());
        return sorted;
    }
    
    public List<Reader> sortByFullName() {
        logger.log(Level.INFO, "Sorting readers by full name");
        List<Reader> sorted = getAll();
        sorted.sort(Reader.byFullName());
        return sorted;
    }
    
    public List<Reader> findByFirstName(String firstName) {
        logger.log(Level.INFO, "Searching readers by first name: {0}", firstName);
        return getAll().stream()
                .filter(reader -> reader.firstName().equalsIgnoreCase(firstName))
                .collect(Collectors.toList());
    }
    
    public List<Reader> findByLastName(String lastName) {
        logger.log(Level.INFO, "Searching readers by last name: {0}", lastName);
        return getAll().stream()
                .filter(reader -> reader.lastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }
    
    public List<Reader> findByFullNameContains(String keyword) {
        logger.log(Level.INFO, "Searching readers by full name containing: {0}", keyword);
        return getAll().stream()
                .filter(reader -> reader.getFullName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public Optional<Reader> findByReaderId(String readerId) {
        logger.log(Level.INFO, "Searching reader by ID: {0}", readerId);
        return getAll().stream()
                .filter(reader -> reader.readerId().equals(readerId))
                .findFirst();
    }
    
    public List<String> getAllFirstNames() {
        logger.log(Level.INFO, "Getting all first names");
        return getAll().stream()
                .map(Reader::firstName)
                .distinct()
                .collect(Collectors.toList());
    }
    
    public Map<String, Long> countByLastName() {
        logger.log(Level.INFO, "Counting readers by last name");
        return getAll().stream()
                .collect(Collectors.groupingBy(Reader::lastName, Collectors.counting()));
    }
    
    public long countByFirstName(String firstName) {
        logger.log(Level.INFO, "Counting readers with first name: {0}", firstName);
        return getAll().stream()
                .filter(reader -> reader.firstName().equalsIgnoreCase(firstName))
                .count();
    }
    
    public List<String> getAllFullNames() {
        logger.log(Level.INFO, "Getting all full names");
        return getAll().stream()
                .map(Reader::getFullName)
                .collect(Collectors.toList());
    }
}

