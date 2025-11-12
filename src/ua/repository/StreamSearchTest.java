package ua.repository;

import ua.library.*;
import ua.enums.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StreamSearchTest {
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    public static void main(String[] args) {
        System.out.println("=== STREAM SEARCH UNIT TESTS ===\n");
        
        testBookRepositorySearch();
        testReaderRepositorySearch();
        testAuthorRepositorySearch();
        testLoanRepositorySearch();
        testMembershipRepositorySearch();
        testTerminalOperations();
        
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
    
    private static void testBookRepositorySearch() {
        System.out.println("--- Test: BookRepository Search ---");
        
        BookRepository repo = new BookRepository();
        Author author = Author.of("Test", "Author", 1980);
        
        Book book1 = Book.of("Test Book", author, "1111111111", BookStatus.AVAILABLE);
        Book book2 = Book.of("Another Book", author, "2222222222", BookStatus.CHECKED_OUT);
        Book book3 = Book.of("Test Novel", author, "3333333333", BookStatus.AVAILABLE);
        
        repo.add(book1);
        repo.add(book2);
        repo.add(book3);
        
        List<Book> found = repo.findByTitle("Test Book");
        assertEquals(1, found.size(), "Should find one book by exact title");
        assertEquals("Test Book", found.get(0).getTitle(), "Found book should be Test Book");
        
        List<Book> foundContains = repo.findByTitleContains("Test");
        assertEquals(2, foundContains.size(), "Should find 2 books containing 'Test'");
        
        List<Book> foundStatus = repo.findByStatus(BookStatus.AVAILABLE);
        assertEquals(2, foundStatus.size(), "Should find 2 books with AVAILABLE status");
        
        List<Book> foundAuthor = repo.findByAuthor(author);
        assertEquals(3, foundAuthor.size(), "Should find all books by author");
        
        List<String> titles = repo.getAllTitles();
        assertEquals(3, titles.size(), "Should get 3 titles");
        assertTrue(titles.contains("Test Book"), "Titles should contain 'Test Book'");
        
        Map<BookStatus, Long> countByStatus = repo.countByStatus();
        assertEquals(2L, countByStatus.get(BookStatus.AVAILABLE), "Should have 2 AVAILABLE books");
        
        System.out.println("BookRepository Search tests passed\n");
    }
    
    private static void testReaderRepositorySearch() {
        System.out.println("--- Test: ReaderRepository Search ---");
        
        ReaderRepository repo = new ReaderRepository();
        
        Reader reader1 = Reader.of("John", "Doe", "RD11111");
        Reader reader2 = Reader.of("Jane", "Doe", "RD22222");
        Reader reader3 = Reader.of("John", "Smith", "RD33333");
        
        repo.add(reader1);
        repo.add(reader2);
        repo.add(reader3);
        
        List<Reader> found = repo.findByFirstName("John");
        assertEquals(2, found.size(), "Should find 2 readers with first name John");
        
        List<Reader> foundLastName = repo.findByLastName("Doe");
        assertEquals(2, foundLastName.size(), "Should find 2 readers with last name Doe");
        
        Optional<Reader> foundById = repo.findByReaderId("RD11111");
        assertTrue(foundById.isPresent(), "Should find reader by ID");
        assertEquals("RD11111", foundById.get().readerId(), "Found reader ID should match");
        
        List<String> firstNames = repo.getAllFirstNames();
        assertEquals(2, firstNames.size(), "Should have 2 unique first names");
        
        Map<String, Long> countByLastName = repo.countByLastName();
        assertEquals(2L, countByLastName.get("Doe"), "Should have 2 readers with last name Doe");
        
        System.out.println("ReaderRepository Search tests passed\n");
    }
    
    private static void testAuthorRepositorySearch() {
        System.out.println("--- Test: AuthorRepository Search ---");
        
        AuthorRepository repo = new AuthorRepository();
        
        Author author1 = Author.of("John", "Doe", 1900);
        Author author2 = Author.of("Jane", "Smith", 1950);
        Author author3 = Author.of("Bob", "Johnson", 2000);
        
        repo.add(author1);
        repo.add(author2);
        repo.add(author3);
        
        List<Author> found = repo.findByBirthYearRange(1900, 1950);
        assertEquals(2, found.size(), "Should find 2 authors in range 1900-1950");
        
        Optional<Author> oldest = repo.findOldest();
        assertTrue(oldest.isPresent(), "Should find oldest author");
        assertEquals(1900, oldest.get().birthYear(), "Oldest author should be born in 1900");
        
        Optional<Author> youngest = repo.findYoungest();
        assertTrue(youngest.isPresent(), "Should find youngest author");
        assertEquals(2000, youngest.get().birthYear(), "Youngest author should be born in 2000");
        
        double avgYear = repo.getAverageBirthYear();
        assertTrue(avgYear > 1900 && avgYear < 2000, "Average year should be between 1900 and 2000");
        
        System.out.println("AuthorRepository Search tests passed\n");
    }
    
    private static void testLoanRepositorySearch() {
        System.out.println("--- Test: LoanRepository Search ---");
        
        LoanRepository repo = new LoanRepository();
        Author author = Author.of("Test", "Author", 1980);
        Book book1 = Book.of("Book 1", author, "1111111111", BookStatus.AVAILABLE);
        Book book2 = Book.of("Book 2", author, "2222222222", BookStatus.AVAILABLE);
        Reader reader1 = Reader.of("Reader", "One", "RD11111");
        Reader reader2 = Reader.of("Reader", "Two", "RD22222");
        
        LocalDate date1 = LocalDate.now().minusDays(20);
        LocalDate date2 = LocalDate.now().minusDays(5);
        
        Loan loan1 = Loan.of(book1, reader1, date1, date1.plusDays(14));
        Loan loan2 = Loan.of(book2, reader2, date2, date2.plusDays(21));
        Loan loan3 = Loan.of(book1, reader1, date2, date2.plusDays(14));
        
        repo.add(loan1);
        repo.add(loan2);
        repo.add(loan3);
        
        List<Loan> foundByReader = repo.findByReader(reader1);
        assertEquals(2, foundByReader.size(), "Should find 2 loans for reader1");
        
        List<Loan> foundByBook = repo.findByBook(book1);
        assertEquals(2, foundByBook.size(), "Should find 2 loans for book1");
        
        Map<Reader, Long> countByReader = repo.countByReader();
        assertEquals(2L, countByReader.get(reader1), "Reader1 should have 2 loans");
        
        Optional<Loan> oldest = repo.findOldestLoan();
        assertTrue(oldest.isPresent(), "Should find oldest loan");
        
        System.out.println("LoanRepository Search tests passed\n");
    }
    
    private static void testMembershipRepositorySearch() {
        System.out.println("--- Test: MembershipRepository Search ---");
        
        MembershipRepository repo = new MembershipRepository();
        Reader reader1 = Reader.of("Reader", "One", "RD11111");
        Reader reader2 = Reader.of("Reader", "Two", "RD22222");
        
        LocalDate start1 = LocalDate.now().minusMonths(6);
        LocalDate start2 = LocalDate.now().minusMonths(3);
        
        Membership membership1 = Membership.of(reader1, start2, start2.plusYears(1), MembershipType.PREMIUM);
        Membership membership2 = Membership.of(reader2, start1, start1.plusYears(1), MembershipType.STANDARD);
        
        repo.add(membership1);
        repo.add(membership2);
        
        List<Membership> foundByType = repo.findByType(MembershipType.PREMIUM);
        assertEquals(1, foundByType.size(), "Should find 1 PREMIUM membership");
        
        List<Membership> active = repo.findActive();
        assertTrue(active.size() >= 1, "Should find at least 1 active membership");
        
        Map<MembershipType, Long> countByType = repo.countByType();
        assertEquals(1L, countByType.get(MembershipType.PREMIUM), "Should have 1 PREMIUM membership");
        
        System.out.println("MembershipRepository Search tests passed\n");
    }
    
    private static void testTerminalOperations() {
        System.out.println("--- Test: Terminal Operations ---");
        
        BookRepository repo = new BookRepository();
        Author author = Author.of("Test", "Author", 1980);
        
        Book book1 = Book.of("Book A", author, "1111111111", BookStatus.AVAILABLE);
        Book book2 = Book.of("Book B", author, "2222222222", BookStatus.AVAILABLE);
        Book book3 = Book.of("Book C", author, "3333333333", BookStatus.CHECKED_OUT);
        
        repo.add(book1);
        repo.add(book2);
        repo.add(book3);
        
        long count = repo.countByTitleContains("Book");
        assertEquals(3L, count, "Should count 3 books containing 'Book'");
        
        Optional<Book> first = repo.findFirstByStatus(BookStatus.AVAILABLE);
        assertTrue(first.isPresent(), "Should find first AVAILABLE book");
        
        repo.getAll().forEach(book -> {
            assertTrue(book != null, "forEach should process all books");
        });
        
        System.out.println("Terminal Operations tests passed\n");
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
    
    private static void assertEquals(long expected, long actual, String message) {
        if (expected == actual) {
            testsPassed++;
        } else {
            testsFailed++;
            System.err.println("FAIL: " + message + " (expected: " + expected + ", actual: " + actual + ")");
        }
    }
    
    private static void assertEquals(double expected, double actual, String message) {
        if (Math.abs(expected - actual) < 0.001) {
            testsPassed++;
        } else {
            testsFailed++;
            System.err.println("FAIL: " + message + " (expected: " + expected + ", actual: " + actual + ")");
        }
    }
}

