package ua.repository;

import ua.library.Reader;
import java.util.List;
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
}

