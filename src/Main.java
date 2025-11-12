import ua.library.*;
import ua.enums.*;
import ua.repository.*;
import java.time.LocalDate;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("=== LIBRARY SYSTEM WITH RECORDS, ENUMS & SWITCH EXPRESSIONS ===\n");
        
        demonstrateRecords();
        demonstrateEnums();
        demonstrateSwitchExpressions();
        demonstrateBookStatus();
        demonstrateMembershipTypes();
        
        System.out.println("\n\n=== GENERIC REPOSITORY DEMONSTRATION ===\n");
        demonstrateRepository();
        
        System.out.println("\n\n=== SORTING DEMONSTRATION ===\n");
        demonstrateSorting();
        
        System.out.println("\n\n=== STREAM API SEARCH DEMONSTRATION ===\n");
        demonstrateStreamSearch();
        
        System.out.println("\n\n=== SERIALIZATION DEMONSTRATION ===\n");
        demonstrateSerialization();
    }
    
    private static void demonstrateRecords() {
        System.out.println("--- 1. Records Demo ---");
        
        Author author1 = new Author("George", "Orwell", 1903);
        Author author2 = Author.of("Isaac", "Asimov", 1920);
        
        System.out.println("Author record: " + author1);
        System.out.println("Full name: " + author1.getFullName());
        System.out.println("Equals: " + author1.equals(author2));
        
        Reader reader1 = Reader.of("Ivan", "Petrov", "RD12345");
        Reader reader2 = Reader.of("Ivan", "Petrov", "RD12345");
        
        System.out.println("Reader record: " + reader1);
        System.out.println("Records equality: " + reader1.equals(reader2));
        System.out.println();
    }
    
    private static void demonstrateEnums() {
        System.out.println("--- 2. Enums Demo ---");
        
        System.out.println("BookStatus values:");
        for (BookStatus status : BookStatus.values()) {
            System.out.println("  " + status + ": " + status.getDescription());
        }
        
        System.out.println("\nMembershipType values:");
        for (MembershipType type : MembershipType.values()) {
            System.out.println("  " + type + ": " + type.getDescription() + 
                             " ($" + type.getMonthlyFee() + "/month)");
        }
        System.out.println();
    }
    
    private static void demonstrateSwitchExpressions() {
        System.out.println("--- 3. Switch Expressions Demo ---");
        
        BookStatus[] statuses = {BookStatus.AVAILABLE, BookStatus.CHECKED_OUT, 
                                 BookStatus.RESERVED, BookStatus.LOST};
        
        for (BookStatus status : statuses) {
            String action = getRecommendedAction(status);
            System.out.println(status + " → " + action);
        }
        
        System.out.println();
        
        MembershipType[] types = MembershipType.values();
        for (MembershipType type : types) {
            String recommendation = getMembershipRecommendation(type);
            System.out.println(type + " → " + recommendation);
        }
        System.out.println();
    }
    
    private static String getRecommendedAction(BookStatus status) {
        return switch (status) {
            case AVAILABLE -> "Can be checked out immediately";
            case CHECKED_OUT -> "Add to waiting list";
            case RESERVED -> "Reserved for another patron";
            case LOST -> "Report to librarian";
        };
    }
    
    private static String getMembershipRecommendation(MembershipType type) {
        return switch (type) {
            case STANDARD -> "Good for casual readers";
            case PREMIUM -> "Best for avid readers";
            case STUDENT -> "Perfect for students with ID";
            case SENIOR -> "Great discount for seniors 65+";
        };
    }
    
    private static void demonstrateBookStatus() {
        System.out.println("--- 4. Book Status in Action ---");
        
        Author author = Author.of("Terry", "Pratchett", 1948);
        
        Book book1 = Book.of("Good Omens", author, "9780060853983", BookStatus.AVAILABLE);
        System.out.println(book1.getTitle() + ": " + book1.getStatusInfo());
        System.out.println("Can checkout? " + book1.getStatus().isAvailableForCheckout());
        
        book1.setStatus(BookStatus.CHECKED_OUT);
        System.out.println("\nAfter checkout:");
        System.out.println(book1.getTitle() + ": " + book1.getStatusInfo());
        System.out.println("Can checkout? " + book1.getStatus().isAvailableForCheckout());
        
        book1.setStatus(BookStatus.RESERVED);
        System.out.println("\nAfter reservation:");
        System.out.println(book1.getTitle() + ": " + book1.getStatusInfo());
        
        Book lostBook = Book.of("Lost Book", author, "1234567890", BookStatus.LOST);
        System.out.println("\n" + lostBook.getTitle() + ": " + lostBook.getStatusInfo());
        System.out.println();
    }
    
    private static void demonstrateMembershipTypes() {
        System.out.println("--- 5. Membership Types Comparison ---");
        
        Reader reader = Reader.of("Maria", "Ivanova", "RD99999");
        
        MembershipType[] allTypes = MembershipType.values();
        
        for (MembershipType type : allTypes) {
            Membership membership = Membership.createYearlyFromNow(reader, type);
            
            System.out.println("\n" + type + " Membership:");
            System.out.println("  Info: " + membership.getMembershipInfo());
            System.out.println("  Monthly fee: $" + type.getMonthlyFee());
            System.out.println("  Max books: " + type.getMaxBooks());
            System.out.println("  Max loan days: " + type.getMaxLoanDays());
            System.out.println("  Active: " + membership.isActive());
            
            String benefit = calculateBenefit(type);
            System.out.println("  Special benefit: " + benefit);
        }
        
        System.out.println("\n=== COMPARISON ===");
        compareMembershipTypes();
        
        System.out.println("\n=== DEMO COMPLETED ===");
    }
    
    private static String calculateBenefit(MembershipType type) {
        return switch (type) {
            case STANDARD -> "Basic library access";
            case PREMIUM -> "Priority reservations + digital library access";
            case STUDENT -> "50% off + study room access";
            case SENIOR -> "50% off + large print books";
        };
    }
    
    private static void compareMembershipTypes() {
        double standardCost = MembershipType.STANDARD.getMonthlyFee() * 12;
        double premiumCost = MembershipType.PREMIUM.getMonthlyFee() * 12;
        double studentCost = MembershipType.STUDENT.getMonthlyFee() * 12;
        double seniorCost = MembershipType.SENIOR.getMonthlyFee() * 12;
        
        System.out.println("Annual costs:");
        System.out.println("  STANDARD: $" + standardCost);
        System.out.println("  PREMIUM: $" + premiumCost);
        System.out.println("  STUDENT: $" + studentCost);
        System.out.println("  SENIOR: $" + seniorCost);
        
        MembershipType bestValue = getBestValue();
        System.out.println("\nBest value: " + bestValue);
    }
    
    private static MembershipType getBestValue() {
        return switch (MembershipType.PREMIUM.getMaxBooks()) {
            case 10 -> MembershipType.PREMIUM;
            case 5 -> MembershipType.STUDENT;
            case 3 -> MembershipType.STANDARD;
            default -> MembershipType.STANDARD;
        };
    }
    
    private static void demonstrateRepository() {
        System.out.println("--- 1. Створення об'єктів різних типів ---");
        
        Author author1 = Author.of("George", "Orwell", 1903);
        Author author2 = Author.of("Isaac", "Asimov", 1920);
        Author author3 = Author.of("Terry", "Pratchett", 1948);
        
        Book book1 = Book.of("1984", author1, "9780451524935", BookStatus.AVAILABLE);
        Book book2 = Book.of("Foundation", author2, "9780553293357", BookStatus.CHECKED_OUT);
        Book book3 = Book.of("Good Omens", author3, "9780060853983", BookStatus.AVAILABLE);
        
        Reader reader1 = Reader.of("Ivan", "Petrov", "RD12345");
        Reader reader2 = Reader.of("Maria", "Ivanova", "RD67890");
        Reader reader3 = Reader.of("Oleg", "Sidorov", "RD11111");
        
        System.out.println("Створено:");
        System.out.println("  - 3 автори: " + author1.getFullName() + ", " + 
                          author2.getFullName() + ", " + author3.getFullName());
        System.out.println("  - 3 книги: " + book1.getTitle() + ", " + 
                          book2.getTitle() + ", " + book3.getTitle());
        System.out.println("  - 3 читачі: " + reader1.getFullName() + ", " + 
                          reader2.getFullName() + ", " + reader3.getFullName());
        
        System.out.println("\n--- 2. Репозиторій для книг (ISBN як унікальний ідентифікатор) ---");
        GenericRepository<Book> bookRepo = new GenericRepository<>(
            book -> book.getIsbn()
        );
        
        System.out.println("Додаємо книги до репозиторію:");
        System.out.println("  Додавання '" + book1.getTitle() + "': " + 
                          (bookRepo.add(book1) ? "успішно" : "не вдалося"));
        System.out.println("  Додавання '" + book2.getTitle() + "': " + 
                          (bookRepo.add(book2) ? "успішно" : "не вдалося"));
        System.out.println("  Додавання '" + book3.getTitle() + "': " + 
                          (bookRepo.add(book3) ? "успішно" : "не вдалося"));
        System.out.println("  Всього книг у репозиторії: " + bookRepo.size());
        
        System.out.println("\n--- 3. Пошук за identity (ISBN) ---");
        String searchIsbn = "9780451524935";
        Book foundBook = bookRepo.findByIdentity(searchIsbn);
        if (foundBook != null) {
            System.out.println("  Знайдено книгу за ISBN " + searchIsbn + ": " + foundBook.getTitle());
        } else {
            System.out.println("  Книга з ISBN " + searchIsbn + " не знайдена");
        }
        
        System.out.println("\n--- 4. Репозиторій для читачів (readerId як унікальний ідентифікатор) ---");
        GenericRepository<Reader> readerRepo = new GenericRepository<>(
            reader -> reader.readerId()
        );
        
        System.out.println("Додаємо читачів до репозиторію:");
        readerRepo.add(reader1);
        readerRepo.add(reader2);
        readerRepo.add(reader3);
        System.out.println("  Всього читачів: " + readerRepo.size());
        
        Reader foundReader = readerRepo.findByIdentity("RD12345");
        if (foundReader != null) {
            System.out.println("  Знайдено читача за ID RD12345: " + foundReader.getFullName());
        }
        
        System.out.println("\n--- 5. Репозиторій для авторів (комбінований ідентифікатор) ---");
        GenericRepository<Author> authorRepo = new GenericRepository<>(
            author -> author.firstName() + "|" + author.lastName() + "|" + author.birthYear()
        );
        
        authorRepo.add(author1);
        authorRepo.add(author2);
        authorRepo.add(author3);
        System.out.println("  Всього авторів: " + authorRepo.size());
        
        String authorIdentity = "George|Orwell|1903";
        Author foundAuthor = authorRepo.findByIdentity(authorIdentity);
        if (foundAuthor != null) {
            System.out.println("  Знайдено автора за identity '" + authorIdentity + "': " + 
                            foundAuthor.getFullName());
        }
        
        System.out.println("\n--- 6. Демонстрація роботи з дублікованими даними ---");
        System.out.println("Спроба додати книгу з існуючим ISBN:");
        Book duplicateBook = Book.of("Another Title", author1, "9780451524935", BookStatus.RESERVED);
        boolean added = bookRepo.add(duplicateBook);
        System.out.println("  Результат: " + (added ? "додано" : "відхилено (дублікат)"));
        System.out.println("  Всього книг після спроби додати дублікат: " + bookRepo.size());
        
        System.out.println("\nСпроба додати того самого читача двічі:");
        boolean addedReader = readerRepo.add(reader1);
        System.out.println("  Результат: " + (addedReader ? "додано" : "відхилено (дублікат)"));
        System.out.println("  Всього читачів: " + readerRepo.size());
        
        System.out.println("\n--- 7. Отримання всіх елементів ---");
        List<Book> allBooks = bookRepo.getAll();
        System.out.println("  Всі книги в репозиторії (" + allBooks.size() + "):");
        for (Book book : allBooks) {
            System.out.println("    - " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
        }
        
        System.out.println("\n--- 8. Видалення елементів ---");
        System.out.println("  Видалення книги '" + book2.getTitle() + "': " + 
                          (bookRepo.remove(book2) ? "успішно" : "не вдалося"));
        System.out.println("  Всього книг після видалення: " + bookRepo.size());
        
        System.out.println("\n--- 9. Репозиторій для позик ---");
        GenericRepository<Loan> loanRepo = new GenericRepository<>(
            loan -> loan.getBook().getIsbn() + "|" + 
                    loan.getReader().readerId() + "|" + 
                    loan.getIssueDate().toString()
        );
        
        LocalDate today = LocalDate.now();
        Loan loan1 = Loan.of(book1, reader1, today, today.plusDays(14));
        Loan loan2 = Loan.of(book3, reader2, today, today.plusDays(21));
        
        loanRepo.add(loan1);
        loanRepo.add(loan2);
        System.out.println("  Додано позик: " + loanRepo.size());
        
        String loanIdentity = book1.getIsbn() + "|" + reader1.readerId() + "|" + today.toString();
        Loan foundLoan = loanRepo.findByIdentity(loanIdentity);
        if (foundLoan != null) {
            System.out.println("  Знайдено позику: " + foundLoan);
        }
        
        System.out.println("\n--- 10. Репозиторій для членства ---");
        GenericRepository<Membership> membershipRepo = new GenericRepository<>(
            membership -> membership.getReader().readerId() + "|" + 
                         membership.getStartDate().toString()
        );
        
        Membership membership1 = Membership.createYearlyFromNow(reader1, MembershipType.PREMIUM);
        Membership membership2 = Membership.createYearlyFromNow(reader2, MembershipType.STANDARD);
        
        membershipRepo.add(membership1);
        membershipRepo.add(membership2);
        System.out.println("  Додано членств: " + membershipRepo.size());
        
        String membershipIdentity = reader1.readerId() + "|" + LocalDate.now().toString();
        Membership foundMembership = membershipRepo.findByIdentity(membershipIdentity);
        if (foundMembership != null) {
            System.out.println("  Знайдено членство: " + foundMembership.getType() + 
                            " для " + foundMembership.getReader().getFullName());
        }
        
        System.out.println("\n=== ДЕМОНСТРАЦІЯ РЕПОЗИТОРІЮ ЗАВЕРШЕНА ===");
    }
    
    private static void demonstrateSorting() {
        System.out.println("--- 1. Сортування книг ---");
        BookRepository bookRepo = new BookRepository();
        
        Author author1 = Author.of("George", "Orwell", 1903);
        Author author2 = Author.of("Isaac", "Asimov", 1920);
        Author author3 = Author.of("Terry", "Pratchett", 1948);
        Author author4 = Author.of("J.K.", "Rowling", 1965);
        
        Book book1 = Book.of("1984", author1, "9780451524935", BookStatus.AVAILABLE);
        Book book2 = Book.of("Foundation", author2, "9780553293357", BookStatus.CHECKED_OUT);
        Book book3 = Book.of("Good Omens", author3, "9780060853983", BookStatus.RESERVED);
        Book book4 = Book.of("Animal Farm", author1, "9780451526342", BookStatus.AVAILABLE);
        
        bookRepo.add(book4);
        bookRepo.add(book1);
        bookRepo.add(book3);
        bookRepo.add(book2);
        
        System.out.println("Книги до сортування:");
        bookRepo.getAll().forEach(b -> System.out.println("  - " + b.getTitle()));
        
        System.out.println("\nСортування за назвою (за замовчуванням):");
        bookRepo.sortByTitle().forEach(b -> System.out.println("  - " + b.getTitle()));
        
        System.out.println("\nСортування за ISBN:");
        bookRepo.sortByIsbn().forEach(b -> System.out.println("  - " + b.getIsbn() + " - " + b.getTitle()));
        
        System.out.println("\nСортування за статусом:");
        bookRepo.sortByStatus().forEach(b -> System.out.println("  - " + b.getStatus() + " - " + b.getTitle()));
        
        System.out.println("\nСортування за назвою (зворотний порядок):");
        bookRepo.sortByTitleDescending().forEach(b -> System.out.println("  - " + b.getTitle()));
        
        System.out.println("\n--- 2. Сортування читачів ---");
        ReaderRepository readerRepo = new ReaderRepository();
        
        Reader reader1 = Reader.of("Ivan", "Petrov", "RD12345");
        Reader reader2 = Reader.of("Maria", "Ivanova", "RD67890");
        Reader reader3 = Reader.of("Oleg", "Sidorov", "RD11111");
        Reader reader4 = Reader.of("Anna", "Kovalenko", "RD99999");
        
        readerRepo.add(reader3);
        readerRepo.add(reader1);
        readerRepo.add(reader4);
        readerRepo.add(reader2);
        
        System.out.println("Читачі до сортування:");
        readerRepo.getAll().forEach(r -> System.out.println("  - " + r.readerId() + " - " + r.getFullName()));
        
        System.out.println("\nСортування за ID читача:");
        readerRepo.sortByReaderId().forEach(r -> System.out.println("  - " + r.readerId() + " - " + r.getFullName()));
        
        System.out.println("\nСортування за ім'ям:");
        readerRepo.sortByFirstName().forEach(r -> System.out.println("  - " + r.getFullName()));
        
        System.out.println("\nСортування за прізвищем:");
        readerRepo.sortByLastName().forEach(r -> System.out.println("  - " + r.getFullName()));
        
        System.out.println("\n--- 3. Сортування авторів ---");
        AuthorRepository authorRepo = new AuthorRepository();
        
        Author author5 = Author.of("Stephen", "King", 1947);
        authorRepo.add(author3);
        authorRepo.add(author1);
        authorRepo.add(author5);
        authorRepo.add(author2);
        authorRepo.add(author4);
        
        System.out.println("Автори до сортування:");
        authorRepo.getAll().forEach(a -> System.out.println("  - " + a.getFullName() + " (" + a.birthYear() + ")"));
        
        System.out.println("\nСортування за прізвищем та ім'ям:");
        authorRepo.sortByName().forEach(a -> System.out.println("  - " + a.getFullName()));
        
        System.out.println("\nСортування за роком народження:");
        authorRepo.sortByBirthYear().forEach(a -> System.out.println("  - " + a.getFullName() + " (" + a.birthYear() + ")"));
        
        System.out.println("\nСортування за роком народження (зворотний порядок):");
        authorRepo.sortByBirthYearDescending().forEach(a -> System.out.println("  - " + a.getFullName() + " (" + a.birthYear() + ")"));
        
        System.out.println("\n--- 4. Сортування позик ---");
        LoanRepository loanRepo = new LoanRepository();
        
        LocalDate date1 = LocalDate.now().minusDays(10);
        LocalDate date2 = LocalDate.now().minusDays(5);
        LocalDate date3 = LocalDate.now().minusDays(15);
        
        Loan loan1 = Loan.of(book1, reader1, date2, date2.plusDays(14));
        Loan loan2 = Loan.of(book2, reader2, date1, date1.plusDays(21));
        Loan loan3 = Loan.of(book3, reader3, date3, date3.plusDays(14));
        
        loanRepo.add(loan2);
        loanRepo.add(loan1);
        loanRepo.add(loan3);
        
        System.out.println("Позики до сортування:");
        loanRepo.getAll().forEach(l -> System.out.println("  - " + l.getBook().getTitle() + " - " + l.getIssueDate()));
        
        System.out.println("\nСортування за датою видачі:");
        loanRepo.sortByIssueDate().forEach(l -> System.out.println("  - " + l.getIssueDate() + " - " + l.getBook().getTitle()));
        
        System.out.println("\nСортування за датою повернення:");
        loanRepo.sortByReturnDate().forEach(l -> System.out.println("  - " + l.getReturnDate() + " - " + l.getBook().getTitle()));
        
        System.out.println("\nСортування за назвою книги:");
        loanRepo.sortByBookTitle().forEach(l -> System.out.println("  - " + l.getBook().getTitle() + " - " + l.getIssueDate()));
        
        System.out.println("\n--- 5. Сортування членства ---");
        MembershipRepository membershipRepo = new MembershipRepository();
        
        LocalDate start1 = LocalDate.now().minusMonths(6);
        LocalDate start2 = LocalDate.now().minusMonths(3);
        LocalDate start3 = LocalDate.now().minusMonths(12);
        
        Membership membership1 = Membership.of(reader1, start2, start2.plusYears(1), MembershipType.PREMIUM);
        Membership membership2 = Membership.of(reader2, start1, start1.plusYears(1), MembershipType.STANDARD);
        Membership membership3 = Membership.of(reader3, start3, start3.plusYears(1), MembershipType.STUDENT);
        
        membershipRepo.add(membership2);
        membershipRepo.add(membership1);
        membershipRepo.add(membership3);
        
        System.out.println("Членства до сортування:");
        membershipRepo.getAll().forEach(m -> System.out.println("  - " + m.getReader().getFullName() + " - " + m.getStartDate()));
        
        System.out.println("\nСортування за датою початку:");
        membershipRepo.sortByStartDate().forEach(m -> System.out.println("  - " + m.getStartDate() + " - " + m.getReader().getFullName()));
        
        System.out.println("\nСортування за типом:");
        membershipRepo.sortByType().forEach(m -> System.out.println("  - " + m.getType() + " - " + m.getReader().getFullName()));
        
        System.out.println("\nСортування за читачем:");
        membershipRepo.sortByReader().forEach(m -> System.out.println("  - " + m.getReader().getFullName() + " - " + m.getType()));
        
        System.out.println("\n--- 6. Сортування через sortByIdentity ---");
        System.out.println("Сортування книг за identity (asc):");
        bookRepo.sortByIdentity("asc").forEach(b -> System.out.println("  - " + b.getTitle()));
        
        System.out.println("\nСортування книг за identity (desc):");
        bookRepo.sortByIdentity("desc").forEach(b -> System.out.println("  - " + b.getTitle()));
        
        System.out.println("\nСортування читачів за identity (asc):");
        readerRepo.sortByIdentity("asc").forEach(r -> System.out.println("  - " + r.readerId() + " - " + r.getFullName()));
        
        System.out.println("\n=== ДЕМОНСТРАЦІЯ СОРТУВАННЯ ЗАВЕРШЕНА ===");
    }
    
    private static void demonstrateStreamSearch() {
        System.out.println("--- 1. Пошук книг ---");
        BookRepository bookRepo = new BookRepository();
        
        Author author1 = Author.of("George", "Orwell", 1903);
        Author author2 = Author.of("Isaac", "Asimov", 1920);
        Author author3 = Author.of("Terry", "Pratchett", 1948);
        
        Book book1 = Book.of("1984", author1, "9780451524935", BookStatus.AVAILABLE);
        Book book2 = Book.of("Animal Farm", author1, "9780451526342", BookStatus.CHECKED_OUT);
        Book book3 = Book.of("Foundation", author2, "9780553293357", BookStatus.AVAILABLE);
        Book book4 = Book.of("Good Omens", author3, "9780060853983", BookStatus.RESERVED);
        
        bookRepo.add(book1);
        bookRepo.add(book2);
        bookRepo.add(book3);
        bookRepo.add(book4);
        
        System.out.println("Пошук за назвою '1984':");
        bookRepo.findByTitle("1984").forEach(b -> System.out.println("  - " + b.getTitle()));
        
        System.out.println("\nПошук за назвою, що містить 'Farm':");
        bookRepo.findByTitleContains("Farm").forEach(b -> System.out.println("  - " + b.getTitle()));
        
        System.out.println("\nПошук за статусом AVAILABLE:");
        bookRepo.findByStatus(BookStatus.AVAILABLE).forEach(b -> System.out.println("  - " + b.getTitle()));
        
        System.out.println("\nПошук за автором:");
        bookRepo.findByAuthor(author1).forEach(b -> System.out.println("  - " + b.getTitle()));
        
        System.out.println("\nВсі назви книг (map + collect):");
        bookRepo.getAllTitles().forEach(title -> System.out.println("  - " + title));
        
        System.out.println("\nВсі автори (flatMap + collect):");
        bookRepo.getAllAuthors().forEach(a -> System.out.println("  - " + a.getFullName()));
        
        System.out.println("\nПідрахунок за статусом (collect):");
        bookRepo.countByStatus().forEach((status, count) -> 
            System.out.println("  - " + status + ": " + count));
        
        System.out.println("\n--- 2. Пошук читачів ---");
        ReaderRepository readerRepo = new ReaderRepository();
        
        Reader reader1 = Reader.of("Ivan", "Petrov", "RD12345");
        Reader reader2 = Reader.of("Maria", "Ivanova", "RD67890");
        Reader reader3 = Reader.of("Ivan", "Sidorov", "RD11111");
        
        readerRepo.add(reader1);
        readerRepo.add(reader2);
        readerRepo.add(reader3);
        
        System.out.println("Пошук за ім'ям 'Ivan':");
        readerRepo.findByFirstName("Ivan").forEach(r -> System.out.println("  - " + r.getFullName()));
        
        System.out.println("\nВсі імена (map + distinct + collect):");
        readerRepo.getAllFirstNames().forEach(name -> System.out.println("  - " + name));
        
        System.out.println("\nПідрахунок за прізвищем (collect):");
        readerRepo.countByLastName().forEach((lastName, count) -> 
            System.out.println("  - " + lastName + ": " + count));
        
        System.out.println("\n--- 3. Пошук авторів ---");
        AuthorRepository authorRepo = new AuthorRepository();
        
        Author author4 = Author.of("Stephen", "King", 1947);
        authorRepo.add(author1);
        authorRepo.add(author2);
        authorRepo.add(author3);
        authorRepo.add(author4);
        
        System.out.println("Пошук за діапазоном років (1900-1950):");
        authorRepo.findByBirthYearRange(1900, 1950).forEach(a -> 
            System.out.println("  - " + a.getFullName() + " (" + a.birthYear() + ")"));
        
        System.out.println("\nНайстаріший автор:");
        authorRepo.findOldest().ifPresent(a -> 
            System.out.println("  - " + a.getFullName() + " (" + a.birthYear() + ")"));
        
        System.out.println("\nСередній рік народження (reduce через average):");
        System.out.println("  - " + authorRepo.getAverageBirthYear());
        
        System.out.println("\n--- 4. Пошук позик ---");
        LoanRepository loanRepo = new LoanRepository();
        
        LocalDate date1 = LocalDate.now().minusDays(20);
        LocalDate date2 = LocalDate.now().minusDays(5);
        LocalDate date3 = LocalDate.now().minusDays(30);
        
        Loan loan1 = Loan.of(book1, reader1, date2, date2.plusDays(14));
        Loan loan2 = Loan.of(book2, reader2, date1, date1.plusDays(21));
        Loan loan3 = Loan.of(book3, reader1, date3, date3.plusDays(14));
        
        loanRepo.add(loan1);
        loanRepo.add(loan2);
        loanRepo.add(loan3);
        
        System.out.println("Пошук позик читача:");
        loanRepo.findByReader(reader1).forEach(l -> 
            System.out.println("  - " + l.getBook().getTitle() + " - " + l.getIssueDate()));
        
        System.out.println("\nПошук прострочених позик:");
        loanRepo.findOverdue().forEach(l -> 
            System.out.println("  - " + l.getBook().getTitle() + " (повернення: " + l.getReturnDate() + ")"));
        
        System.out.println("\nПідрахунок позик за читачем (collect):");
        loanRepo.countByReader().forEach((reader, count) -> 
            System.out.println("  - " + reader.getFullName() + ": " + count));
        
        System.out.println("\n--- 5. Пошук членства ---");
        MembershipRepository membershipRepo = new MembershipRepository();
        
        LocalDate start1 = LocalDate.now().minusMonths(6);
        LocalDate start2 = LocalDate.now().minusMonths(3);
        
        Membership membership1 = Membership.of(reader1, start2, start2.plusYears(1), MembershipType.PREMIUM);
        Membership membership2 = Membership.of(reader2, start1, start1.plusYears(1), MembershipType.STANDARD);
        Membership membership3 = Membership.of(reader3, start1, start1.plusYears(1), MembershipType.STUDENT);
        
        membershipRepo.add(membership1);
        membershipRepo.add(membership2);
        membershipRepo.add(membership3);
        
        System.out.println("Активні членства:");
        membershipRepo.findActive().forEach(m -> 
            System.out.println("  - " + m.getReader().getFullName() + " - " + m.getType()));
        
        System.out.println("\nПошук за типом PREMIUM:");
        membershipRepo.findByType(MembershipType.PREMIUM).forEach(m -> 
            System.out.println("  - " + m.getReader().getFullName()));
        
        System.out.println("\nПідрахунок за типом (collect):");
        membershipRepo.countByType().forEach((type, count) -> 
            System.out.println("  - " + type + ": " + count));
        
        System.out.println("\n--- 6. Термінальні операції: forEach ---");
        System.out.println("Виведення всіх книг через forEach:");
        bookRepo.getAll().forEach(book -> {
            System.out.println("  - " + book.getTitle() + " (" + book.getStatus() + ")");
        });
        
        System.out.println("\n--- 7. Термінальні операції: reduce ---");
        System.out.println("Сума років народження авторів (reduce):");
        int totalYears = authorRepo.getAll().stream()
                .mapToInt(Author::birthYear)
                .reduce(0, Integer::sum);
        System.out.println("  - Сума: " + totalYears);
        
        System.out.println("\nМаксимальний рік народження (reduce):");
        int maxYear = authorRepo.getAll().stream()
                .mapToInt(Author::birthYear)
                .reduce(Integer.MIN_VALUE, Integer::max);
        System.out.println("  - Максимум: " + maxYear);
        
        System.out.println("\n--- 8. Порівняння stream vs parallelStream ---");
        List<Book> largeBookList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            largeBookList.add(Book.of("Book " + i, author1, "ISBN" + i, BookStatus.AVAILABLE));
        }
        BookRepository largeRepo = new BookRepository();
        largeBookList.forEach(largeRepo::add);
        
        System.out.println("Тестування на " + largeRepo.size() + " книгах");
        
        long startTime = System.nanoTime();
        long count1 = largeRepo.getAll().stream()
                .filter(book -> book.getTitle().contains("5"))
                .count();
        long streamTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        long count2 = largeRepo.getAll().parallelStream()
                .filter(book -> book.getTitle().contains("5"))
                .count();
        long parallelTime = System.nanoTime() - startTime;
        
        System.out.println("Stream результат: " + count1 + " (час: " + streamTime / 1_000_000 + " мс)");
        System.out.println("ParallelStream результат: " + count2 + " (час: " + parallelTime / 1_000_000 + " мс)");
        System.out.println("Прискорення: " + String.format("%.2f", (double) streamTime / parallelTime) + "x");
        
        System.out.println("\n=== ДЕМОНСТРАЦІЯ ПОШУКУ ТА STREAM API ЗАВЕРШЕНА ===");
    }
    
    private static void demonstrateSerialization() {
        System.out.println("--- 1. Завантаження конфігурації ---");
        System.out.println("JSON path для books: " + ua.util.ConfigLoader.getJsonPath("books"));
        System.out.println("YAML path для books: " + ua.util.ConfigLoader.getYamlPath("books"));
        System.out.println("Кількість тестових книг: " + ua.util.ConfigLoader.getTestDataCount("books"));
        
        System.out.println("\n--- 2. Генерація тестових даних ---");
        ua.repository.AuthorRepository authorRepo = ua.util.TestDataGenerator.generateAuthorRepository();
        ua.repository.BookRepository bookRepo = ua.util.TestDataGenerator.generateBookRepository();
        ua.repository.ReaderRepository readerRepo = ua.util.TestDataGenerator.generateReaderRepository();
        ua.repository.LoanRepository loanRepo = ua.util.TestDataGenerator.generateLoanRepository(bookRepo, readerRepo);
        ua.repository.MembershipRepository membershipRepo = ua.util.TestDataGenerator.generateMembershipRepository(readerRepo);
        
        System.out.println("Створено:");
        System.out.println("  - Авторів: " + authorRepo.size());
        System.out.println("  - Книг: " + bookRepo.size());
        System.out.println("  - Читачів: " + readerRepo.size());
        System.out.println("  - Позик: " + loanRepo.size());
        System.out.println("  - Членств: " + membershipRepo.size());
        
        System.out.println("\n--- 3. Збереження у JSON та YAML ---");
        try {
            ua.repository.RepositorySerializer.saveBookRepository(bookRepo);
            ua.repository.RepositorySerializer.saveReaderRepository(readerRepo);
            ua.repository.RepositorySerializer.saveAuthorRepository(authorRepo);
            ua.repository.RepositorySerializer.saveLoanRepository(loanRepo);
            ua.repository.RepositorySerializer.saveMembershipRepository(membershipRepo);
            System.out.println("Всі репозиторії успішно збережено");
        } catch (ua.util.DataSerializationException e) {
            System.err.println("Помилка збереження: " + e.getMessage());
        }
        
        System.out.println("\n--- 4. Завантаження з JSON ---");
        try {
            ua.repository.BookRepository loadedBookRepo = ua.repository.RepositorySerializer.loadBookRepository();
            ua.repository.ReaderRepository loadedReaderRepo = ua.repository.RepositorySerializer.loadReaderRepository();
            ua.repository.AuthorRepository loadedAuthorRepo = ua.repository.RepositorySerializer.loadAuthorRepository();
            ua.repository.LoanRepository loadedLoanRepo = ua.repository.RepositorySerializer.loadLoanRepository();
            ua.repository.MembershipRepository loadedMembershipRepo = ua.repository.RepositorySerializer.loadMembershipRepository();
            
            System.out.println("Завантажено:");
            System.out.println("  - Авторів: " + loadedAuthorRepo.size());
            System.out.println("  - Книг: " + loadedBookRepo.size());
            System.out.println("  - Читачів: " + loadedReaderRepo.size());
            System.out.println("  - Позик: " + loadedLoanRepo.size());
            System.out.println("  - Членств: " + loadedMembershipRepo.size());
            
            System.out.println("\n--- 5. Порівняння даних ---");
            boolean booksMatch = bookRepo.size() == loadedBookRepo.size();
            boolean readersMatch = readerRepo.size() == loadedReaderRepo.size();
            boolean authorsMatch = authorRepo.size() == loadedAuthorRepo.size();
            
            System.out.println("Кількість книг збігається: " + booksMatch);
            System.out.println("Кількість читачів збігається: " + readersMatch);
            System.out.println("Кількість авторів збігається: " + authorsMatch);
            
            if (booksMatch && loadedBookRepo.size() > 0) {
                Book original = bookRepo.getAll().get(0);
                Book loaded = loadedBookRepo.getAll().get(0);
                System.out.println("\nПорівняння першої книги:");
                System.out.println("  Оригінал: " + original.getTitle() + " (" + original.getIsbn() + ")");
                System.out.println("  Завантажена: " + loaded.getTitle() + " (" + loaded.getIsbn() + ")");
                System.out.println("  Збігаються: " + original.getTitle().equals(loaded.getTitle()) && 
                                 original.getIsbn().equals(loaded.getIsbn()));
            }
            
            if (readersMatch && loadedReaderRepo.size() > 0) {
                Reader original = readerRepo.getAll().get(0);
                Reader loaded = loadedReaderRepo.getAll().get(0);
                System.out.println("\nПорівняння першого читача:");
                System.out.println("  Оригінал: " + original.getFullName() + " (" + original.readerId() + ")");
                System.out.println("  Завантажений: " + loaded.getFullName() + " (" + loaded.readerId() + ")");
                System.out.println("  Збігаються: " + original.readerId().equals(loaded.readerId()));
            }
            
        } catch (ua.util.DataSerializationException e) {
            System.err.println("Помилка завантаження: " + e.getMessage());
        }
        
        System.out.println("\n--- 6. Завантаження з YAML ---");
        try {
            ua.repository.BookRepository yamlBookRepo = ua.repository.RepositorySerializer.loadBookRepositoryFromYaml();
            System.out.println("Завантажено книг з YAML: " + yamlBookRepo.size());
            System.out.println("YAML завантаження успішне");
        } catch (ua.util.DataSerializationException e) {
            System.err.println("Помилка завантаження YAML: " + e.getMessage());
        }
        
        System.out.println("\n--- 7. Демонстрація обробки винятків ---");
        try {
            ua.util.DataSerializer.loadFromJson("nonexistent_file.json", Book.class);
            System.err.println("Повинна бути помилка для неіснуючого файлу");
        } catch (ua.util.DataSerializationException e) {
            System.out.println("Виняток оброблено коректно: " + e.getMessage());
        }
        
        try {
            ua.util.DataSerializer.loadFromYaml("nonexistent_file.yaml", Reader.class);
            System.err.println("Повинна бути помилка для неіснуючого файлу");
        } catch (ua.util.DataSerializationException e) {
            System.out.println("Виняток оброблено коректно: " + e.getMessage());
        }
        
        System.out.println("\n=== ДЕМОНСТРАЦІЯ СЕРІАЛІЗАЦІЇ ЗАВЕРШЕНА ===");
    }
}
