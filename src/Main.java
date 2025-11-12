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
}
