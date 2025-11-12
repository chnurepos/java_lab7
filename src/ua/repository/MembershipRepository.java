package ua.repository;

import ua.library.Membership;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class MembershipRepository extends GenericRepository<Membership> {
    
    private static final Logger logger = Logger.getLogger(MembershipRepository.class.getName());
    
    public MembershipRepository() {
        super(membership -> membership.getReader().readerId() + "|" + 
                         membership.getStartDate().toString());
        logger.log(Level.INFO, "MembershipRepository initialized");
    }
    
    public List<Membership> sortByStartDate() {
        logger.log(Level.INFO, "Sorting memberships by start date");
        List<Membership> sorted = getAll();
        sorted.sort(Membership::compareTo);
        return sorted;
    }
    
    public List<Membership> sortByEndDate() {
        logger.log(Level.INFO, "Sorting memberships by end date");
        List<Membership> sorted = getAll();
        sorted.sort(Membership.byEndDate());
        return sorted;
    }
    
    public List<Membership> sortByType() {
        logger.log(Level.INFO, "Sorting memberships by type");
        List<Membership> sorted = getAll();
        sorted.sort(Membership.byType());
        return sorted;
    }
    
    public List<Membership> sortByReader() {
        logger.log(Level.INFO, "Sorting memberships by reader");
        List<Membership> sorted = getAll();
        sorted.sort(Membership.byReader());
        return sorted;
    }
    
    public List<Membership> sortByStartDateDescending() {
        logger.log(Level.INFO, "Sorting memberships by start date descending");
        List<Membership> sorted = getAll();
        sorted.sort(Membership.byStartDateDescending());
        return sorted;
    }
}

