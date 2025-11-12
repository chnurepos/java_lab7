package ua.repository;

import ua.library.*;
import ua.enums.*;
import java.time.LocalDate;
import java.util.List;

public class SortingTest {
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    public static void main(String[] args) {
        System.out.println("=== SORTING UNIT TESTS ===\n");
        
        testBookSorting();
        testReaderSorting();
        testAuthorSorting();
        testLoanSorting();
        testMembershipSorting();
        testSortByIdentity();
        
        System.out.println("\n=== TEST RESULTS ===");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);
        System.out.println("Total: " + (testsPassed + testsFailed));
        
        if (testsFailed == 0) {
            System.out.println("\n✓ All tests passed!");
        } else {
            System.out.println("\n✗ Some tests failed!");
        }
    }
    
    private static void testBookSorting() {
        System.out.println("--- Test: Book Sorting ---");
        
        BookRepository repo = new BookRepository();
        Author author = Author.of("Test", "Author", 1980);
        
        Book book1 = Book.of("Zebra", author, "9999999999", BookStatus.AVAILABLE);
        Book book2 = Book.of("Apple", author, "1111111111", BookStatus.CHECKED_OUT);
        Book book3 = Book.of("Banana", author, "2222222222", BookStatus.RESERVED);
        
        repo.add(book3);
        repo.add(book1);
        repo.add(book2);
        
        List<Book> sortedByTitle = repo.sortByTitle();
        assertEquals("Apple", sortedByTitle.get(0).getTitle(), "First book should be Apple");
        assertEquals("Banana", sortedByTitle.get(1).getTitle(), "Second book should be Banana");
        assertEquals("Zebra", sortedByTitle.get(2).getTitle(), "Third book should be Zebra");
        
        List<Book> sortedByIsbn = repo.sortByIsbn();
        assertEquals("1111111111", sortedByIsbn.get(0).getIsbn(), "First ISBN should be 1111111111");
        
        System.out.println("✓ Book Sorting tests passed\n");
    }
    
    private static void testReaderSorting() {
        System.out.println("--- Test: Reader Sorting ---");
        
        ReaderRepository repo = new ReaderRepository();
        
        Reader reader1 = Reader.of("Zoe", "Adams", "RD99999");
        Reader reader2 = Reader.of("Alice", "Brown", "RD11111");
        Reader reader3 = Reader.of("Bob", "Adams", "RD22222");
        
        repo.add(reader3);
        repo.add(reader1);
        repo.add(reader2);
        
        List<Reader> sortedById = repo.sortByReaderId();
        assertEquals("RD11111", sortedById.get(0).readerId(), "First reader ID should be RD11111");
        
        List<Reader> sortedByFirstName = repo.sortByFirstName();
        assertEquals("Alice", sortedByFirstName.get(0).firstName(), "First name should be Alice");
        
        List<Reader> sortedByLastName = repo.sortByLastName();
        assertEquals("Adams", sortedByLastName.get(0).lastName(), "First last name should be Adams");
        
        System.out.println("✓ Reader Sorting tests passed\n");
    }
    
    private static void testAuthorSorting() {
        System.out.println("--- Test: Author Sorting ---");
        
        AuthorRepository repo = new AuthorRepository();
        
        Author author1 = Author.of("Zoe", "Zebra", 2000);
        Author author2 = Author.of("Alice", "Apple", 1900);
        Author author3 = Author.of("Bob", "Apple", 1950);
        
        repo.add(author3);
        repo.add(author1);
        repo.add(author2);
        
        List<Author> sortedByName = repo.sortByName();
        assertEquals("Apple", sortedByName.get(0).lastName(), "First last name should be Apple");
        
        List<Author> sortedByYear = repo.sortByBirthYear();
        assertEquals(1900, sortedByYear.get(0).birthYear(), "First birth year should be 1900");
        
        List<Author> sortedByYearDesc = repo.sortByBirthYearDescending();
        assertEquals(2000, sortedByYearDesc.get(0).birthYear(), "First birth year descending should be 2000");
        
        System.out.println("✓ Author Sorting tests passed\n");
    }
    
    private static void testLoanSorting() {
        System.out.println("--- Test: Loan Sorting ---");
        
        LoanRepository repo = new LoanRepository();
        Author author = Author.of("Test", "Author", 1980);
        Book book1 = Book.of("Book A", author, "1111111111", BookStatus.AVAILABLE);
        Book book2 = Book.of("Book B", author, "2222222222", BookStatus.AVAILABLE);
        Reader reader = Reader.of("Test", "Reader", "RD99999");
        
        LocalDate date1 = LocalDate.now().minusDays(10);
        LocalDate date2 = LocalDate.now().minusDays(5);
        
        Loan loan1 = Loan.of(book1, reader, date2, date2.plusDays(14));
        Loan loan2 = Loan.of(book2, reader, date1, date1.plusDays(14));
        
        repo.add(loan1);
        repo.add(loan2);
        
        List<Loan> sortedByIssueDate = repo.sortByIssueDate();
        assertTrue(sortedByIssueDate.get(0).getIssueDate().isBefore(sortedByIssueDate.get(1).getIssueDate()), 
                  "First loan should have earlier issue date");
        
        List<Loan> sortedByBookTitle = repo.sortByBookTitle();
        assertEquals("Book A", sortedByBookTitle.get(0).getBook().getTitle(), "First book title should be Book A");
        
        System.out.println("✓ Loan Sorting tests passed\n");
    }
    
    private static void testMembershipSorting() {
        System.out.println("--- Test: Membership Sorting ---");
        
        MembershipRepository repo = new MembershipRepository();
        Reader reader1 = Reader.of("Alice", "A", "RD11111");
        Reader reader2 = Reader.of("Bob", "B", "RD22222");
        
        LocalDate start1 = LocalDate.now().minusMonths(6);
        LocalDate start2 = LocalDate.now().minusMonths(3);
        
        Membership membership1 = Membership.of(reader1, start2, start2.plusYears(1), MembershipType.PREMIUM);
        Membership membership2 = Membership.of(reader2, start1, start1.plusYears(1), MembershipType.STANDARD);
        
        repo.add(membership1);
        repo.add(membership2);
        
        List<Membership> sortedByStartDate = repo.sortByStartDate();
        assertTrue(sortedByStartDate.get(0).getStartDate().isBefore(sortedByStartDate.get(1).getStartDate()),
                  "First membership should have earlier start date");
        
        List<Membership> sortedByType = repo.sortByType();
        assertEquals(MembershipType.PREMIUM, sortedByType.get(1).getType(), "Second type should be PREMIUM");
        
        System.out.println("✓ Membership Sorting tests passed\n");
    }
    
    private static void testSortByIdentity() {
        System.out.println("--- Test: sortByIdentity ---");
        
        BookRepository bookRepo = new BookRepository();
        Author author = Author.of("Test", "Author", 1980);
        
        Book book1 = Book.of("A", author, "1111111111", BookStatus.AVAILABLE);
        Book book2 = Book.of("B", author, "2222222222", BookStatus.AVAILABLE);
        Book book3 = Book.of("C", author, "3333333333", BookStatus.AVAILABLE);
        
        bookRepo.add(book3);
        bookRepo.add(book1);
        bookRepo.add(book2);
        
        List<Book> sortedAsc = bookRepo.sortByIdentity("asc");
        assertEquals("A", sortedAsc.get(0).getTitle(), "First book in asc should be A");
        assertEquals("C", sortedAsc.get(2).getTitle(), "Last book in asc should be C");
        
        List<Book> sortedDesc = bookRepo.sortByIdentity("desc");
        assertEquals("C", sortedDesc.get(0).getTitle(), "First book in desc should be C");
        assertEquals("A", sortedDesc.get(2).getTitle(), "Last book in desc should be A");
        
        ReaderRepository readerRepo = new ReaderRepository();
        Reader reader1 = Reader.of("A", "A", "RD11111");
        Reader reader2 = Reader.of("B", "B", "RD22222");
        
        readerRepo.add(reader2);
        readerRepo.add(reader1);
        
        List<Reader> sortedReaders = readerRepo.sortByIdentity("asc");
        assertEquals("RD11111", sortedReaders.get(0).readerId(), "First reader ID should be RD11111");
        
        System.out.println("✓ sortByIdentity tests passed\n");
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

