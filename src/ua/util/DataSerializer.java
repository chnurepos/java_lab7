package ua.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DataSerializer {
    
    private static final Logger logger = Logger.getLogger(DataSerializer.class.getName());
    private static final ObjectMapper jsonMapper;
    private static final ObjectMapper yamlMapper;
    
    static {
        jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        
        yamlMapper = new ObjectMapper(new YAMLFactory());
        yamlMapper.registerModule(new JavaTimeModule());
    }
    
    public static <T> void saveToJson(List<T> data, String filePath, Class<T> type) throws DataSerializationException {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            
            jsonMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            logger.log(Level.INFO, "Successfully saved {0} items to JSON file: {1}", 
                      new Object[]{data.size(), filePath});
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving to JSON file {0}: {1}", 
                      new Object[]{filePath, e.getMessage()});
            throw new DataSerializationException("Failed to save data to JSON: " + filePath, e);
        }
    }
    
    public static <T> List<T> loadFromJson(String filePath, Class<T> type) throws DataSerializationException {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                logger.log(Level.WARNING, "JSON file not found: {0}", filePath);
                throw new DataSerializationException("File not found: " + filePath);
            }
            
            TypeFactory typeFactory = jsonMapper.getTypeFactory();
            CollectionType collectionType = typeFactory.constructCollectionType(List.class, type);
            List<T> data = jsonMapper.readValue(file, collectionType);
            
            logger.log(Level.INFO, "Successfully loaded {0} items from JSON file: {1}", 
                      new Object[]{data.size(), filePath});
            return data;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading from JSON file {0}: {1}", 
                      new Object[]{filePath, e.getMessage()});
            throw new DataSerializationException("Failed to load data from JSON: " + filePath, e);
        }
    }
    
    public static <T> void saveToYaml(List<T> data, String filePath, Class<T> type) throws DataSerializationException {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            
            yamlMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            logger.log(Level.INFO, "Successfully saved {0} items to YAML file: {1}", 
                      new Object[]{data.size(), filePath});
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving to YAML file {0}: {1}", 
                      new Object[]{filePath, e.getMessage()});
            throw new DataSerializationException("Failed to save data to YAML: " + filePath, e);
        }
    }
    
    public static <T> List<T> loadFromYaml(String filePath, Class<T> type) throws DataSerializationException {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                logger.log(Level.WARNING, "YAML file not found: {0}", filePath);
                throw new DataSerializationException("File not found: " + filePath);
            }
            
            TypeFactory typeFactory = yamlMapper.getTypeFactory();
            CollectionType collectionType = typeFactory.constructCollectionType(List.class, type);
            List<T> data = yamlMapper.readValue(file, collectionType);
            
            logger.log(Level.INFO, "Successfully loaded {0} items from YAML file: {1}", 
                      new Object[]{data.size(), filePath});
            return data;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading from YAML file {0}: {1}", 
                      new Object[]{filePath, e.getMessage()});
            throw new DataSerializationException("Failed to load data from YAML: " + filePath, e);
        }
    }
}

