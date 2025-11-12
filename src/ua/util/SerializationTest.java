package ua.util;

import ua.library.*;
import ua.enums.*;
import ua.repository.*;
import java.time.LocalDate;
import java.util.List;

public class SerializationTest {
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    public static void main(String[] args) {
        System.out.println("=== SERIALIZATION UNIT TESTS ===\n");
        
        testJsonSerialization();
        testYamlSerialization();
        testExceptionHandling();
        testDataIntegrity();
        
        System.out.println("\n=== TEST RESULTS ===");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);
        System.out.println("Total: " + (testsPassed + testsFailed));
        
        if (testsFailed == 0) {
            System.out.println("\nAll tests passed!");
        } else {
            System.out.println("\nSome tests failed!");
        }
    }
    
    private static void testJsonSerialization() {
        System.out.println("--- Test: JSON Serialization ---");
        
        try {
            BookRepository repo = new BookRepository();
            Author author = Author.of("Test", "Author", 1980);
            Book book1 = Book.of("Test Book 1", author, "1111111111", BookStatus.AVAILABLE);
            Book book2 = Book.of("Test Book 2", author, "2222222222", BookStatus.CHECKED_OUT);
            
            repo.add(book1);
            repo.add(book2);
            
            String testPath = "data/test_books.json";
            DataSerializer.saveToJson(repo.getAll(), testPath, Book.class);
            assertTrue(true, "JSON save should succeed");
            
            List<Book> loaded = DataSerializer.loadFromJson(testPath, Book.class);
            assertEquals(2, loaded.size(), "Should load 2 books");
            assertEquals("Test Book 1", loaded.get(0).getTitle(), "First book title should match");
            
            System.out.println("JSON Serialization tests passed\n");
        } catch (DataSerializationException e) {
            testsFailed++;
            System.err.println("FAIL: JSON serialization error: " + e.getMessage());
        }
    }
    
    private static void testYamlSerialization() {
        System.out.println("--- Test: YAML Serialization ---");
        
        try {
            ReaderRepository repo = new ReaderRepository();
            Reader reader1 = Reader.of("John", "Doe", "RD11111");
            Reader reader2 = Reader.of("Jane", "Smith", "RD22222");
            
            repo.add(reader1);
            repo.add(reader2);
            
            String testPath = "data/test_readers.yaml";
            DataSerializer.saveToYaml(repo.getAll(), testPath, Reader.class);
            assertTrue(true, "YAML save should succeed");
            
            List<Reader> loaded = DataSerializer.loadFromYaml(testPath, Reader.class);
            assertEquals(2, loaded.size(), "Should load 2 readers");
            assertEquals("RD11111", loaded.get(0).readerId(), "First reader ID should match");
            
            System.out.println("YAML Serialization tests passed\n");
        } catch (DataSerializationException e) {
            testsFailed++;
            System.err.println("FAIL: YAML serialization error: " + e.getMessage());
        }
    }
    
    private static void testExceptionHandling() {
        System.out.println("--- Test: Exception Handling ---");
        
        try {
            DataSerializer.loadFromJson("nonexistent.json", Book.class);
            testsFailed++;
            System.err.println("FAIL: Should throw exception for nonexistent file");
        } catch (DataSerializationException e) {
            assertTrue(true, "Should throw DataSerializationException for nonexistent file");
        }
        
        try {
            DataSerializer.loadFromYaml("nonexistent.yaml", Reader.class);
            testsFailed++;
            System.err.println("FAIL: Should throw exception for nonexistent file");
        } catch (DataSerializationException e) {
            assertTrue(true, "Should throw DataSerializationException for nonexistent YAML file");
        }
        
        System.out.println("Exception Handling tests passed\n");
    }
    
    private static void testDataIntegrity() {
        System.out.println("--- Test: Data Integrity ---");
        
        try {
            AuthorRepository repo = new AuthorRepository();
            Author author1 = Author.of("George", "Orwell", 1903);
            Author author2 = Author.of("Isaac", "Asimov", 1920);
            
            repo.add(author1);
            repo.add(author2);
            
            String testPath = "data/test_authors.json";
            DataSerializer.saveToJson(repo.getAll(), testPath, Author.class);
            
            List<Author> loaded = DataSerializer.loadFromJson(testPath, Author.class);
            assertEquals(2, loaded.size(), "Should load 2 authors");
            
            Author loaded1 = loaded.get(0);
            assertEquals("George", loaded1.firstName(), "First name should match");
            assertEquals("Orwell", loaded1.lastName(), "Last name should match");
            assertEquals(1903, loaded1.birthYear(), "Birth year should match");
            
            System.out.println("Data Integrity tests passed\n");
        } catch (DataSerializationException e) {
            testsFailed++;
            System.err.println("FAIL: Data integrity error: " + e.getMessage());
        }
    }
    
    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            testsPassed++;
        } else {
            testsFailed++;
            System.err.println("FAIL: " + message);
        }
    }
    
    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) {
            testsPassed++;
            return;
        }
        if (expected != null && expected.equals(actual)) {
            testsPassed++;
        } else {
            testsFailed++;
            System.err.println("FAIL: " + message + " (expected: " + expected + ", actual: " + actual + ")");
        }
    }
    
    private static void assertEquals(int expected, int actual, String message) {
        if (expected == actual) {
            testsPassed++;
        } else {
            testsFailed++;
            System.err.println("FAIL: " + message + " (expected: " + expected + ", actual: " + actual + ")");
        }
    }
}

