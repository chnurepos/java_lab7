package ua.repository;

import ua.library.*;
import ua.enums.*;
import java.time.LocalDate;
import java.util.List;

public class GenericRepositoryTest {
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    public static void main(String[] args) {
        System.out.println("=== GENERIC REPOSITORY UNIT TESTS ===\n");
        
        testBookRepository();
        testReaderRepository();
        testAuthorRepository();
        testLoanRepository();
        testMembershipRepository();
        testDuplicateHandling();
        testNullHandling();
        testEmptyRepository();
        
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
    
    private static void testBookRepository() {
        System.out.println("--- Test: Book Repository ---");
        
        GenericRepository<Book> repo = new GenericRepository<>(
            book -> book.getIsbn()
        );
        
        Author author = Author.of("Test", "Author", 1980);
        Book book1 = Book.of("Book 1", author, "1234567890", BookStatus.AVAILABLE);
        Book book2 = Book.of("Book 2", author, "0987654321", BookStatus.CHECKED_OUT);
        
        assertTrue(repo.isEmpty(), "Repository should be empty initially");
        assertTrue(repo.add(book1), "Should add book1");
        assertTrue(repo.add(book2), "Should add book2");
        assertEquals(2, repo.size(), "Repository should have 2 items");
        
        Book found = repo.findByIdentity("1234567890");
        assertNotNull(found, "Should find book by ISBN");
        assertEquals(book1, found, "Found book should equal book1");
        
        assertTrue(repo.remove(book1), "Should remove book1");
        assertEquals(1, repo.size(), "Repository should have 1 item after removal");
        assertNull(repo.findByIdentity("1234567890"), "Should not find removed book");
        
        List<Book> all = repo.getAll();
        assertEquals(1, all.size(), "getAll should return 1 item");
        assertTrue(all.contains(book2), "getAll should contain book2");
        
        System.out.println("Book Repository tests passed\n");
    }
    
    private static void testReaderRepository() {
        System.out.println("--- Test: Reader Repository ---");
        
        GenericRepository<Reader> repo = new GenericRepository<>(
            reader -> reader.readerId()
        );
        
        Reader reader1 = Reader.of("John", "Doe", "RD00001");
        Reader reader2 = Reader.of("Jane", "Smith", "RD00002");
        
        assertTrue(repo.add(reader1), "Should add reader1");
        assertTrue(repo.add(reader2), "Should add reader2");
        
        Reader found = repo.findByIdentity("RD00001");
        assertNotNull(found, "Should find reader by ID");
        assertEquals(reader1, found, "Found reader should equal reader1");
        
        assertTrue(repo.remove(reader1), "Should remove reader1");
        assertNull(repo.findByIdentity("RD00001"), "Should not find removed reader");
        
        System.out.println("Reader Repository tests passed\n");
    }
    
    private static void testAuthorRepository() {
        System.out.println("--- Test: Author Repository ---");
        
        GenericRepository<Author> repo = new GenericRepository<>(
            author -> author.firstName() + "|" + author.lastName() + "|" + author.birthYear()
        );
        
        Author author1 = Author.of("George", "Orwell", 1903);
        Author author2 = Author.of("Isaac", "Asimov", 1920);
        
        assertTrue(repo.add(author1), "Should add author1");
        assertTrue(repo.add(author2), "Should add author2");
        
        String identity = "George|Orwell|1903";
        Author found = repo.findByIdentity(identity);
        assertNotNull(found, "Should find author by identity");
        assertEquals(author1, found, "Found author should equal author1");
        
        System.out.println("Author Repository tests passed\n");
    }
    
    private static void testLoanRepository() {
        System.out.println("--- Test: Loan Repository ---");
        
        GenericRepository<Loan> repo = new GenericRepository<>(
            loan -> loan.getBook().getIsbn() + "|" + 
                    loan.getReader().readerId() + "|" + 
                    loan.getIssueDate().toString()
        );
        
        Author author = Author.of("Test", "Author", 1980);
        Book book = Book.of("Test Book", author, "1111111111", BookStatus.AVAILABLE);
        Reader reader = Reader.of("Test", "Reader", "RD99999");
        LocalDate issueDate = LocalDate.now();
        LocalDate returnDate = issueDate.plusDays(14);
        
        Loan loan = Loan.of(book, reader, issueDate, returnDate);
        
        assertTrue(repo.add(loan), "Should add loan");
        assertEquals(1, repo.size(), "Repository should have 1 loan");
        
        String identity = book.getIsbn() + "|" + reader.readerId() + "|" + issueDate.toString();
        Loan found = repo.findByIdentity(identity);
        assertNotNull(found, "Should find loan by identity");
        
        System.out.println("Loan Repository tests passed\n");
    }
    
    private static void testMembershipRepository() {
        System.out.println("--- Test: Membership Repository ---");
        
        GenericRepository<Membership> repo = new GenericRepository<>(
            membership -> membership.getReader().readerId() + "|" + 
                         membership.getStartDate().toString()
        );
        
        Reader reader = Reader.of("Test", "Reader", "RD88888");
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusYears(1);
        Membership membership = Membership.of(reader, start, end, MembershipType.STANDARD);
        
        assertTrue(repo.add(membership), "Should add membership");
        
        String identity = reader.readerId() + "|" + start.toString();
        Membership found = repo.findByIdentity(identity);
        assertNotNull(found, "Should find membership by identity");
        
        System.out.println("Membership Repository tests passed\n");
    }
    
    private static void testDuplicateHandling() {
        System.out.println("--- Test: Duplicate Handling ---");
        
        GenericRepository<Book> repo = new GenericRepository<>(
            book -> book.getIsbn()
        );
        
        Author author = Author.of("Test", "Author", 1980);
        Book book1 = Book.of("Book 1", author, "1234567890", BookStatus.AVAILABLE);
        Book book2 = Book.of("Book 2", author, "1234567890", BookStatus.CHECKED_OUT);
        
        assertTrue(repo.add(book1), "Should add first book");
        assertFalse(repo.add(book2), "Should not add duplicate ISBN");
        assertEquals(1, repo.size(), "Repository should still have 1 item");
        
        assertFalse(repo.add(book1), "Should not add same book twice");
        
        System.out.println("Duplicate Handling tests passed\n");
    }
    
    private static void testNullHandling() {
        System.out.println("--- Test: Null Handling ---");
        
        GenericRepository<Book> repo = new GenericRepository<>(
            book -> book.getIsbn()
        );
        
        assertFalse(repo.add(null), "Should not add null item");
        assertFalse(repo.remove(null), "Should not remove null item");
        assertNull(repo.findByIdentity(null), "Should return null for null identity");
        
        System.out.println("Null Handling tests passed\n");
    }
    
    private static void testEmptyRepository() {
        System.out.println("--- Test: Empty Repository ---");
        
        GenericRepository<Book> repo = new GenericRepository<>(
            book -> book.getIsbn()
        );
        
        assertTrue(repo.isEmpty(), "Repository should be empty");
        assertEquals(0, repo.size(), "Size should be 0");
        assertTrue(repo.getAll().isEmpty(), "getAll should return empty list");
        
        repo.clear();
        assertTrue(repo.isEmpty(), "Repository should be empty after clear");
        
        System.out.println("Empty Repository tests passed\n");
    }
    
    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            testsPassed++;
        } else {
            testsFailed++;
            System.err.println("FAIL: " + message);
        }
    }
    
    private static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
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
    
    private static void assertNotNull(Object obj, String message) {
        if (obj != null) {
            testsPassed++;
        } else {
            testsFailed++;
            System.err.println("FAIL: " + message);
        }
    }
    
    private static void assertNull(Object obj, String message) {
        if (obj == null) {
            testsPassed++;
        } else {
            testsFailed++;
            System.err.println("FAIL: " + message);
        }
    }
}

