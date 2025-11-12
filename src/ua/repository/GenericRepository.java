package ua.repository;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class GenericRepository<T> {
    
    private static final Logger logger = Logger.getLogger(GenericRepository.class.getName());
    
    private final List<T> items;
    private final IdentityExtractor<T> identityExtractor;
    private final Map<Object, T> identityMap;
    
    public GenericRepository(IdentityExtractor<T> identityExtractor) {
        if (identityExtractor == null) {
            throw new IllegalArgumentException("Identity extractor cannot be null");
        }
        this.items = new ArrayList<>();
        this.identityExtractor = identityExtractor;
        this.identityMap = new HashMap<>();
        
        logger.log(Level.INFO, "GenericRepository initialized");
    }
    
    public boolean add(T item) {
        if (item == null) {
            logger.log(Level.WARNING, "Attempt to add null item");
            return false;
        }
        
        Object identity = identityExtractor.extractIdentity(item);
        
        if (identityMap.containsKey(identity)) {
            logger.log(Level.WARNING, "Item with identity {0} already exists", identity);
            return false;
        }
        
        items.add(item);
        identityMap.put(identity, item);
        logger.log(Level.INFO, "Item added successfully. Identity: {0}, Total items: {1}", 
                   new Object[]{identity, items.size()});
        return true;
    }
    
    public boolean remove(T item) {
        if (item == null) {
            logger.log(Level.WARNING, "Attempt to remove null item");
            return false;
        }
        
        Object identity = identityExtractor.extractIdentity(item);
        
        if (!identityMap.containsKey(identity)) {
            logger.log(Level.WARNING, "Item with identity {0} not found for removal", identity);
            return false;
        }
        
        items.remove(item);
        identityMap.remove(identity);
        logger.log(Level.INFO, "Item removed successfully. Identity: {0}, Remaining items: {1}", 
                   new Object[]{identity, items.size()});
        return true;
    }
    
    public List<T> getAll() {
        logger.log(Level.FINE, "Retrieving all items. Total: {0}", items.size());
        return Collections.unmodifiableList(new ArrayList<>(items));
    }
    
    public T findByIdentity(Object identity) {
        if (identity == null) {
            logger.log(Level.WARNING, "Attempt to find item with null identity");
            return null;
        }
        
        T found = identityMap.get(identity);
        
        if (found != null) {
            logger.log(Level.FINE, "Item found by identity: {0}", identity);
        } else {
            logger.log(Level.FINE, "Item not found by identity: {0}", identity);
        }
        
        return found;
    }
    
    public int size() {
        return items.size();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public void clear() {
        int size = items.size();
        items.clear();
        identityMap.clear();
        logger.log(Level.INFO, "Repository cleared. Removed {0} items", size);
    }
    
    @Override
    public String toString() {
        return "GenericRepository{size=" + items.size() + "}";
    }
}

