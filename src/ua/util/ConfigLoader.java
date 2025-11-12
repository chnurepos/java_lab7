package ua.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ConfigLoader {
    
    private static final Logger logger = Logger.getLogger(ConfigLoader.class.getName());
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;
    
    static {
        loadConfig();
    }
    
    private static void loadConfig() {
        properties = new Properties();
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                logger.log(Level.WARNING, "Config file not found, using defaults");
                return;
            }
            properties.load(input);
            logger.log(Level.INFO, "Config file loaded successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading config file: {0}", e.getMessage());
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public static int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid integer value for key {0}, using default: {1}", 
                      new Object[]{key, defaultValue});
            return defaultValue;
        }
    }
    
    public static String getJsonPath(String entity) {
        return getProperty("json." + entity + ".path", "data/" + entity + ".json");
    }
    
    public static String getYamlPath(String entity) {
        return getProperty("yaml." + entity + ".path", "data/" + entity + ".yaml");
    }
    
    public static int getTestDataCount(String entity) {
        return getIntProperty("test.data." + entity + ".count", 5);
    }
}

