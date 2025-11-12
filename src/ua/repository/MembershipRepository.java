package ua.repository;

import ua.library.Membership;
import ua.library.Reader;
import ua.enums.MembershipType;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
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
    
    public List<Membership> findByReader(Reader reader) {
        logger.log(Level.INFO, "Searching memberships by reader: {0}", reader.readerId());
        return getAll().stream()
                .filter(membership -> membership.getReader().equals(reader))
                .collect(Collectors.toList());
    }
    
    public List<Membership> findByType(MembershipType type) {
        logger.log(Level.INFO, "Searching memberships by type: {0}", type);
        return getAll().stream()
                .filter(membership -> membership.getType() == type)
                .collect(Collectors.toList());
    }
    
    public List<Membership> findByStartDateRange(LocalDate startDate, LocalDate endDate) {
        logger.log(Level.INFO, "Searching memberships by start date range: {0} - {1}", new Object[]{startDate, endDate});
        return getAll().stream()
                .filter(membership -> !membership.getStartDate().isBefore(startDate) && 
                        !membership.getStartDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
    
    public List<Membership> findActive() {
        logger.log(Level.INFO, "Searching active memberships");
        return getAll().stream()
                .filter(Membership::isActive)
                .collect(Collectors.toList());
    }
    
    public List<Membership> findExpired() {
        logger.log(Level.INFO, "Searching expired memberships");
        return getAll().stream()
                .filter(Membership::isExpired)
                .collect(Collectors.toList());
    }
    
    public List<Membership> findByEndDateRange(LocalDate startDate, LocalDate endDate) {
        logger.log(Level.INFO, "Searching memberships by end date range: {0} - {1}", new Object[]{startDate, endDate});
        return getAll().stream()
                .filter(membership -> !membership.getEndDate().isBefore(startDate) && 
                        !membership.getEndDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
    
    public Map<MembershipType, Long> countByType() {
        logger.log(Level.INFO, "Counting memberships by type");
        return getAll().stream()
                .collect(Collectors.groupingBy(Membership::getType, Collectors.counting()));
    }
    
    public long countActive() {
        logger.log(Level.INFO, "Counting active memberships");
        return getAll().stream()
                .filter(Membership::isActive)
                .count();
    }
    
    public Optional<Membership> findOldestMembership() {
        logger.log(Level.INFO, "Finding oldest membership");
        return getAll().stream()
                .min(Membership::compareTo);
    }
}

