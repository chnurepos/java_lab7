package ua.util;

import ua.library.*;
import ua.enums.*;
import java.time.LocalDate;

public class ValidationTest {
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    public static void main(String[] args) {
        System.out.println("=== VALIDATION UNIT TESTS ===\n");
        
        testValidData();
        testInvalidBookData();
        testInvalidAuthorData();
        testInvalidReaderData();
        testInvalidLoanData();
        testInvalidMembershipData();
        testSettersValidation();
        
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
    
    private static void testValidData() {
        System.out.println("--- Test: Valid Data Creation ---");
        
        try {
            Author author = Author.of("George", "Orwell", 1903);
            Book book = Book.of("1984", author, "9780451524935", BookStatus.AVAILABLE);
            Reader reader = Reader.of("John", "Doe", "RD12345");
            Loan loan = Loan.of(book, reader, LocalDate.now(), LocalDate.now().plusDays(14));
            Membership membership = Membership.of(reader, LocalDate.now(), LocalDate.now().plusYears(1), MembershipType.STANDARD);
            
            assertTrue(author != null, "Author should be created");
            assertTrue(book != null, "Book should be created");
            assertTrue(reader != null, "Reader should be created");
            assertTrue(loan != null, "Loan should be created");
            assertTrue(membership != null, "Membership should be created");
            
            System.out.println("Valid Data Creation tests passed\n");
        } catch (InvalidDataException e) {
            testsFailed++;
            System.err.println("FAIL: Valid data should not throw exception: " + e.getMessage());
        }
    }
    
    private static void testInvalidBookData() {
        System.out.println("--- Test: Invalid Book Data ---");
        
        try {
            Author author = Author.of("Test", "Author", 1980);
            Book.of("", author, "9780451524935", BookStatus.AVAILABLE);
            testsFailed++;
            System.err.println("FAIL: Should throw exception for empty title");
        } catch (InvalidDataException e) {
            assertTrue(e.getErrors().size() > 0, "Exception should contain errors");
            assertTrue(e.getMessage().contains("title"), "Error should mention title");
            testsPassed++;
        }
        
        try {
            Author author = Author.of("Test", "Author", 1980);
            Book.of("Test Book", author, "123", BookStatus.AVAILABLE);
            testsFailed++;
            System.err.println("FAIL: Should throw exception for invalid ISBN");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("isbn"), "Error should mention ISBN");
            testsPassed++;
        }
        
        try {
            Author author = Author.of("Test", "Author", 1980);
            Book.of("Test Book", null, "9780451524935", BookStatus.AVAILABLE);
            testsFailed++;
            System.err.println("FAIL: Should throw exception for null authors");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("authors"), "Error should mention authors");
            testsPassed++;
        }
        
        System.out.println("Invalid Book Data tests passed\n");
    }
    
    private static void testInvalidAuthorData() {
        System.out.println("--- Test: Invalid Author Data ---");
        
        try {
            Author.of("", "Orwell", 1903);
            testsFailed++;
            System.err.println("FAIL: Should throw exception for empty firstName");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("firstName"), "Error should mention firstName");
            testsPassed++;
        }
        
        try {
            Author.of("George", "", 1903);
            testsFailed++;
            System.err.println("FAIL: Should throw exception for empty lastName");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("lastName"), "Error should mention lastName");
            testsPassed++;
        }
        
        try {
            Author.of("George", "Orwell", 500);
            testsFailed++;
            System.err.println("FAIL: Should throw exception for invalid birthYear");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("birthYear"), "Error should mention birthYear");
            testsPassed++;
        }
        
        System.out.println("Invalid Author Data tests passed\n");
    }
    
    private static void testInvalidReaderData() {
        System.out.println("--- Test: Invalid Reader Data ---");
        
        try {
            Reader.of("", "Doe", "RD12345");
            testsFailed++;
            System.err.println("FAIL: Should throw exception for empty firstName");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("firstName"), "Error should mention firstName");
            testsPassed++;
        }
        
        try {
            Reader.of("John", "Doe", "abc");
            testsFailed++;
            System.err.println("FAIL: Should throw exception for invalid readerId");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("readerId"), "Error should mention readerId");
            testsPassed++;
        }
        
        System.out.println("Invalid Reader Data tests passed\n");
    }
    
    private static void testInvalidLoanData() {
        System.out.println("--- Test: Invalid Loan Data ---");
        
        try {
            Author author = Author.of("Test", "Author", 1980);
            Book book = Book.of("Test Book", author, "9780451524935", BookStatus.AVAILABLE);
            Reader reader = Reader.of("John", "Doe", "RD12345");
            
            Loan.of(null, reader, LocalDate.now(), LocalDate.now().plusDays(14));
            testsFailed++;
            System.err.println("FAIL: Should throw exception for null book");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("book"), "Error should mention book");
            testsPassed++;
        }
        
        try {
            Author author = Author.of("Test", "Author", 1980);
            Book book = Book.of("Test Book", author, "9780451524935", BookStatus.AVAILABLE);
            Reader reader = Reader.of("John", "Doe", "RD12345");
            
            LocalDate start = LocalDate.now();
            LocalDate end = start.minusDays(1);
            Loan.of(book, reader, start, end);
            testsFailed++;
            System.err.println("FAIL: Should throw exception for invalid date range");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("loan period"), "Error should mention loan period");
            testsPassed++;
        }
        
        System.out.println("Invalid Loan Data tests passed\n");
    }
    
    private static void testInvalidMembershipData() {
        System.out.println("--- Test: Invalid Membership Data ---");
        
        try {
            Reader reader = Reader.of("John", "Doe", "RD12345");
            LocalDate start = LocalDate.now();
            LocalDate end = start.minusDays(1);
            Membership.of(reader, start, end, MembershipType.STANDARD);
            testsFailed++;
            System.err.println("FAIL: Should throw exception for invalid date range");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("membership period"), "Error should mention membership period");
            testsPassed++;
        }
        
        try {
            Membership.of(null, LocalDate.now(), LocalDate.now().plusYears(1), MembershipType.STANDARD);
            testsFailed++;
            System.err.println("FAIL: Should throw exception for null reader");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("reader"), "Error should mention reader");
            testsPassed++;
        }
        
        System.out.println("Invalid Membership Data tests passed\n");
    }
    
    private static void testSettersValidation() {
        System.out.println("--- Test: Setters Validation ---");
        
        try {
            Author author = Author.of("Test", "Author", 1980);
            Book book = Book.of("Test Book", author, "9780451524935", BookStatus.AVAILABLE);
            
            book.setTitle("");
            testsFailed++;
            System.err.println("FAIL: Should throw exception for empty title in setter");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("title"), "Error should mention title");
            testsPassed++;
        }
        
        try {
            Author author = Author.of("Test", "Author", 1980);
            Book book = Book.of("Test Book", author, "9780451524935", BookStatus.AVAILABLE);
            
            book.setIsbn("123");
            testsFailed++;
            System.err.println("FAIL: Should throw exception for invalid ISBN in setter");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("isbn"), "Error should mention ISBN");
            testsPassed++;
        }
        
        try {
            Reader reader = Reader.of("John", "Doe", "RD12345");
            Author author = Author.of("Test", "Author", 1980);
            Book book = Book.of("Test Book", author, "9780451524935", BookStatus.AVAILABLE);
            Loan loan = Loan.of(book, reader, LocalDate.now(), LocalDate.now().plusDays(14));
            
            LocalDate invalidDate = LocalDate.now().minusDays(1);
            loan.setReturnDate(invalidDate);
            testsFailed++;
            System.err.println("FAIL: Should throw exception for invalid return date in setter");
        } catch (InvalidDataException e) {
            assertTrue(e.getMessage().contains("loan period"), "Error should mention loan period");
            testsPassed++;
        }
        
        System.out.println("Setters Validation tests passed\n");
    }
    
    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            testsPassed++;
        } else {
            testsFailed++;
            System.err.println("FAIL: " + message);
        }
    }
}

