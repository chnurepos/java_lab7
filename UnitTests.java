package ua.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import ua.library.*;
import ua.enums.*;
import ua.repository.*;
import ua.util.InvalidDataException;

import java.time.LocalDate;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LibrarySystemTest {

    private Author testAuthor;
    private Book testBook;
    private Reader testReader;

    @BeforeEach
    void setUp() throws InvalidDataException {
        testAuthor = new Author("Douglas", "Adams", 1952);
        testBook = new Book("Hitchhiker's Guide", testAuthor, "9780345391803", BookStatus.AVAILABLE);
        testReader = Reader.of("Arthur", "Dent", "RD00001");
    }

    @Test
    void testAuthorRecordCreationAndGetters() {
        assertEquals("Douglas", testAuthor.firstName());
        assertEquals("Adams", testAuthor.lastName());
        assertEquals(1952, testAuthor.birthYear());
        assertEquals("Douglas Adams", testAuthor.getFullName());
    }
    
    @Test
    void testAuthorRecordEquality() throws InvalidDataException {
        Author sameAuthor = Author.of("Douglas", "Adams", 1952);
        Author differentAuthor = Author.of("Terry", "Pratchett", 1948);
        
        assertEquals(testAuthor, sameAuthor);
        assertNotEquals(testAuthor, differentAuthor);
    }
    
    @Test
    void testBookCreationAndStatusInfo() throws InvalidDataException {
        assertEquals("Hitchhiker's Guide", testBook.getTitle());
        assertEquals(testAuthor, testBook.getAuthor());
        assertEquals("9780345391803", testBook.getIsbn());
        assertEquals(BookStatus.AVAILABLE, testBook.getStatus());
        assertTrue(testBook.getStatusInfo().contains("доступна"));
        
        testBook.setStatus(BookStatus.CHECKED_OUT);
        assertTrue(testBook.getStatusInfo().contains("видана"));
    }
    
    @Test
    void testReaderCreationAndGetters() {
        assertEquals("Arthur", testReader.firstName());
        assertEquals("Dent", testReader.lastName());
        assertEquals("RD00001", testReader.readerId());
    }

    @Test
    void testInvalidRecordCreationThrowsException() {
        assertThrows(InvalidDataException.class, () -> 
            new Author(null, "Test", 2000));
        assertThrows(InvalidDataException.class, () -> 
            new Book("", testAuthor, "123", BookStatus.AVAILABLE));
        assertThrows(InvalidDataException.class, () -> 
            Reader.of("A", "B", "123456789012345"));
    }
    
    @Test
    void testLoanCreation() throws InvalidDataException {
        LocalDate issue = LocalDate.now();
        LocalDate due = issue.plusDays(14);
        Loan loan = Loan.of(testBook, testReader, issue, due);
        
        assertEquals(testBook, loan.getBook());
        assertEquals(testReader, loan.getReader());
        assertEquals(issue, loan.getIssueDate());
        assertEquals(due, loan.getReturnDate());
        assertFalse(loan.isOverdue());
        
        Loan overdueLoan = Loan.of(testBook, testReader, issue.minusMonths(2), issue.minusMonths(1));
        assertTrue(overdueLoan.isOverdue(issue));
    }
    
    @Test
    void testMembershipCreation() throws InvalidDataException {
        Membership membership = Membership.createYearlyFromNow(testReader, MembershipType.PREMIUM);
        
        assertEquals(testReader, membership.getReader());
        assertEquals(MembershipType.PREMIUM, membership.getType());
        assertTrue(membership.isActive());
    }
    
    @ParameterizedTest
    @EnumSource(BookStatus.class)
    void testBookStatusDescriptions(BookStatus status) {
        assertNotNull(status.getDescription());
        assertFalse(status.getDescription().isEmpty());
    }

    @Test
    void testBookStatusAvailability() {
        assertTrue(BookStatus.AVAILABLE.isAvailableForCheckout());
        assertFalse(BookStatus.CHECKED_OUT.isAvailableForCheckout());
        assertFalse(BookStatus.RESERVED.isAvailableForCheckout());
        assertFalse(BookStatus.LOST.isAvailableForCheckout());
    }

    @ParameterizedTest
    @EnumSource(MembershipType.class)
    void testMembershipTypeProperties(MembershipType type) {
        assertTrue(type.getMonthlyFee() >= 0);
        assertTrue(type.getMaxBooks() > 0);
        assertTrue(type.getMaxLoanDays() > 0);
    }
    
    private GenericRepository<Book> bookRepo;
    
    @BeforeEach
    void setupRepository() {
        bookRepo = new GenericRepository<>(Book::getIsbn);
    }
    
    @Test
    void testAddAndSize() {
        assertTrue(bookRepo.add(testBook));
        assertEquals(1, bookRepo.size());
    }
    
    @Test
    void testAddDuplicate() throws InvalidDataException {
        assertTrue(bookRepo.add(testBook));
        Book duplicateIsbn = Book.of("Another Title", testAuthor, testBook.getIsbn(), BookStatus.RESERVED);
        
        assertFalse(bookRepo.add(duplicateIsbn));
        assertEquals(1, bookRepo.size());
    }
    
    @Test
    void testFindByIdentity() {
        bookRepo.add(testBook);
        Book found = bookRepo.findByIdentity(testBook.getIsbn());
        assertEquals(testBook, found);
        assertNull(bookRepo.findByIdentity("NON_EXISTENT_ISBN"));
    }
    
    @Test
    void testRemove() {
        bookRepo.add(testBook);
        assertTrue(bookRepo.remove(testBook));
        assertEquals(0, bookRepo.size());
        assertFalse(bookRepo.remove(testBook));
    }
    
    @Test
    void testGetAll() throws InvalidDataException {
        bookRepo.add(testBook);
        Book book2 = Book.of("The Martian", testAuthor, "9780804139024", BookStatus.AVAILABLE);
        bookRepo.add(book2);
        
        List<Book> allBooks = bookRepo.getAll();
        assertEquals(2, allBooks.size());
        assertTrue(allBooks.contains(testBook));
        assertTrue(allBooks.contains(book2));
    }
    
    private BookRepository bookRepository;
    private ReaderRepository readerRepository;
    private AuthorRepository authorRepository;

    @BeforeEach
    void setupSpecializedRepositories() throws InvalidDataException {
        bookRepository = new BookRepository();
        readerRepository = new ReaderRepository();
        authorRepository = new AuthorRepository();
        
        Author a1 = Author.of("George", "Orwell", 1903);
        Author a2 = Author.of("Isaac", "Asimov", 1920);
        Author a3 = Author.of("Terry", "Pratchett", 1948);
        
        bookRepository.add(Book.of("1984", a1, "9780451524935", BookStatus.AVAILABLE));
        bookRepository.add(Book.of("Foundation", a2, "9780553293357", BookStatus.CHECKED_OUT));
        bookRepository.add(Book.of("Good Omens", a3, "9780060853983", BookStatus.AVAILABLE));

        readerRepository.add(Reader.of("Ivan", "Petrov", "RD12345"));
        readerRepository.add(Reader.of("Maria", "Ivanova", "RD67890"));
        
        authorRepository.add(a1);
        authorRepository.add(a2);
        authorRepository.add(a3);
        authorRepository.add(Author.of("Stephen", "King", 1947));
    }

    @Test
    void testBookSortByTitle() {
        List<Book> sorted = bookRepository.sortByTitle();
        assertEquals("1984", sorted.get(0).getTitle());
        assertEquals("Foundation", sorted.get(1).getTitle());
        assertEquals("Good Omens", sorted.get(2).getTitle());
    }

    @Test
    void testBookSortByStatus() {
        List<Book> sorted = bookRepository.sortByStatus();
        assertEquals(BookStatus.AVAILABLE, sorted.get(0).getStatus());
        assertEquals(BookStatus.CHECKED_OUT, sorted.get(2).getStatus());
    }

    @Test
    void testReaderSortByLastName() {
        List<Reader> sorted = readerRepository.sortByLastName();
        assertEquals("Ivanova", sorted.get(0).lastName());
        assertEquals("Petrov", sorted.get(1).lastName());
    }

    @Test
    void testAuthorSortByBirthYear() {
        List<Author> sorted = authorRepository.sortByBirthYear();
        assertEquals(1903, sorted.get(0).birthYear());
        assertEquals(1947, sorted.get(1).birthYear());
        assertEquals(1948, sorted.get(2).birthYear());
    }
    
    @Test
    void testBookFindByStatus() {
        List<Book> availableBooks = bookRepository.findByStatus(BookStatus.AVAILABLE);
        assertEquals(2, availableBooks.size());
        
        List<Book> checkedOutBooks = bookRepository.findByStatus(BookStatus.CHECKED_OUT);
        assertEquals(1, checkedOutBooks.size());
    }
    
    @Test
    void testBookFindByAuthor() throws InvalidDataException {
        Author a1 = Author.of("George", "Orwell", 1903);
        List<Book> booksByA1 = bookRepository.findByAuthor(a1);
        assertEquals(1, booksByA1.size());
        assertEquals("1984", booksByA1.get(0).getTitle());
    }

    @Test
    void testBookCountByStatus() {
        Map<BookStatus, Long> counts = bookRepository.countByStatus();
        assertEquals(2L, counts.get(BookStatus.AVAILABLE));
        assertEquals(1L, counts.get(BookStatus.CHECKED_OUT));
        assertFalse(counts.containsKey(BookStatus.LOST));
    }
    
    @Test
    void testReaderFindByFirstName() {
        List<Reader> ivans = readerRepository.findByFirstName("Ivan");
        assertEquals(1, ivans.size());
        assertEquals("Petrov", ivans.get(0).lastName());
    }
    
    @Test
    void testAuthorFindOldestAndAverageYear() {
        authorRepository.findOldest().ifPresent(a -> 
            assertEquals(1903, a.birthYear()));
            
        OptionalDouble average = authorRepository.getAverageBirthYear();
        assertTrue(average.isPresent());
        assertEquals(1929.5, average.getAsDouble(), 0.001);
    }
    
    @Test
    void testLoanFindOverdue() throws InvalidDataException {
        LoanRepository loanRepo = new LoanRepository();
        
        LocalDate overdueIssue = LocalDate.now().minusDays(30);
        Loan loan1 = Loan.of(testBook, testReader, overdueIssue, overdueIssue.plusDays(14));
        LocalDate activeIssue = LocalDate.now().minusDays(5);
        Loan loan2 = Loan.of(testBook, testReader, activeIssue, activeIssue.plusDays(14));
        
        loanRepo.add(loan1);
        loanRepo.add(loan2);
        
        List<Loan> overdueLoans = loanRepo.findOverdue();
        assertEquals(1, overdueLoans.size());
        assertEquals(loan1, overdueLoans.get(0));
    }

    @Test
    void testMembershipFindActive() throws InvalidDataException {
        MembershipRepository membershipRepo = new MembershipRepository();
        
        Membership m1 = Membership.createYearlyFromNow(testReader, MembershipType.PREMIUM);
        LocalDate pastEnd = LocalDate.now().minusDays(1);
        Membership m2 = Membership.of(testReader, pastEnd.minusYears(1), pastEnd, MembershipType.STANDARD);
        
        membershipRepo.add(m1);
        membershipRepo.add(m2);
        
        List<Membership> activeMemberships = membershipRepo.findActive();
        assertEquals(1, activeMemberships.size());
        assertEquals(m1, activeMemberships.get(0));
    }
}
