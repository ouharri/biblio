package com.biblio.dao;

import com.biblio.app.Enums.Language;
import com.biblio.app.Models.*;
import com.biblio.libs.Model;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class BookDao extends Model {

    private Book book = null;

    public BookDao() {
        super("books", new String[]{"isbn"});
        this.book = new Book();
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        try {
            String query = "SELECT DISTINCT" +
                    "       b.isbn," +
                    "       b.quantities," +
                    "       b.pages," +
                    "       b.title," +
                    "       b.edition," +
                    "       b.language," +
                    "       b.description," +
                    "       a.id AS author_id," +
                    "       a.first_name AS author_firstName," +
                    "       a.last_name AS author_lastName," +
                    "       c.category," +
                    "       c.description AS category_description " +
                    "FROM books b " +
                    "LEFT JOIN books_authors ba ON b.isbn = ba.book " +
                    "LEFT JOIN authors a ON ba.author = a.id " +
                    "LEFT JOIN categories_books cb ON b.isbn = cb.book " +
                    "LEFT JOIN categories c ON cb.category = c.id " +
                    "WHERE b.delete_at IS NULL " +
                    "ORDER BY b.isbn;";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            executeAndWrrapQuery(books, preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
    public List<Book> search(String keywords) {
        List<Book> books = new ArrayList<>();

        try {
            String query = "SELECT DISTINCT" +
                    "       b.isbn," +
                    "       b.quantities," +
                    "       b.pages," +
                    "       b.title," +
                    "       b.edition," +
                    "       b.language," +
                    "       b.description," +
                    "       a.id AS author_id," +
                    "       a.first_name AS author_firstName," +
                    "       a.last_name AS author_lastName," +
                    "       c.category," +
                    "       c.description AS category_description " +
                    "FROM books b " +
                    "LEFT JOIN books_authors ba ON b.isbn = ba.book " +
                    "LEFT JOIN authors a ON ba.author = a.id " +
                    "LEFT JOIN categories_books cb ON b.isbn = cb.book " +
                    "LEFT JOIN categories c ON cb.category = c.id " +
                    "WHERE b.delete_at IS NULL " +
                    "AND (b.isbn LIKE ? OR b.title LIKE ? OR b.description LIKE ? OR a.first_name LIKE ? OR a.last_name LIKE ? OR c.category LIKE ?) " +
                    "ORDER BY b.isbn;";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            preparedStatement.setString(1,  keywords);
            preparedStatement.setString(2, "%" + keywords + "%");
            preparedStatement.setString(3, "%" + keywords + "%");
            preparedStatement.setString(4, "%" + keywords + "%");
            preparedStatement.setString(5, "%" + keywords + "%");
            preparedStatement.setString(6, "%" + keywords + "%");

            executeAndWrrapQuery(books, preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
    public Book insert(String isbn , String title, String description, Language lang, int quantity, int pages, String edition, int[] author_id, int[] category_id) throws SQLException {

        this.book.setBook(
                isbn,
                quantity,
                pages,
                title,
                edition,
                Language.valueOf(String.valueOf(lang)),
                description
        );

        Map<String, String> bookData = this.book.getBook();

        this.beginTransaction();

        String id = super.create(bookData);

        if (id != null) {

            try(CategorieBookDao categorieBookDao = new CategorieBookDao()){
                for (int j : category_id) {
                    Map<String, String> categorieBookData = new HashMap<String, String>();
                    categorieBookData.put("category", String.valueOf(j));
                    categorieBookData.put("book", isbn);
                    if (categorieBookDao.create(categorieBookData) == null) {
                        this.rollbackTransaction();
                        return null;
                    }
                }
            } catch (Exception e) {
                this.rollbackTransaction();
                return null;
            }

            try(BooksAuthorsDao authorBookDao = new BooksAuthorsDao()){
                for (int j : category_id) {
                    Map<String, String> authorBook = new HashMap<String, String>();
                    authorBook.put("author", String.valueOf(j));
                    authorBook.put("book", isbn);
                    if (authorBookDao.create(authorBook) == null) {
                        this.rollbackTransaction();
                        return null;
                    }
                }
            } catch (Exception e) {
                this.rollbackTransaction();
                return null;
            }

        }

        this.commitTransaction();

        return getBookWithDetails();
    }
    public Book update(String isbn, String title, String description, Language lang, int quantity, int pages, String edition, int[] author_id, int[] category_id) throws SQLException {

        this.book.setBook(
                isbn,
                quantity,
                pages,
                title,
                edition,
                Language.valueOf(lang.toString()),
                description
        );

        this.beginTransaction();

        boolean updated = super.update(this.book.getBook(), new String[]{isbn});

        if (updated) {
            try (CategorieBookDao categorieBookDao = new CategorieBookDao()) {
                if(!categorieBookDao.delete(new String[]{isbn})){
                    this.rollbackTransaction();
                    return null;
                } else {
                    for (int j : category_id) {
                        Map<String, String> categorieBookData = new HashMap<String, String>();
                        categorieBookData.put("category", String.valueOf(j));
                        categorieBookData.put("book", isbn);
                        if (categorieBookDao.create(categorieBookData) == null) {
                            this.rollbackTransaction();
                            return null;
                        }
                    }
                }
            } catch (Exception e) {
                this.rollbackTransaction();
                return null;
            }

            try (BooksAuthorsDao authorBookDao = new BooksAuthorsDao()) {
                if(!authorBookDao.delete(new String[]{isbn})){
                    this.rollbackTransaction();
                    return null;
                } else {
                    for (int j : category_id) {
                        Map<String, String> authorBook = new HashMap<String, String>();
                        authorBook.put("author", String.valueOf(j));
                        authorBook.put("book", isbn);
                        if (authorBookDao.create(authorBook) == null) {
                            this.rollbackTransaction();
                            return null;
                        }
                    }
                }
            } catch (Exception e) {
                this.rollbackTransaction();
                return null;
            }
        } else {
            this.rollbackTransaction();
        }

        this.commitTransaction();

        return getBookWithDetails();
    }
    public boolean delete(String isbn) {
        return super.softDelete(new String[]{isbn});
    }

    public Book find(String isbn) {
        Book book = null;

        try {
            String query = "SELECT DISTINCT" +
                    "       b.isbn," +
                    "       b.quantities," +
                    "       b.pages," +
                    "       b.title," +
                    "       b.edition," +
                    "       b.language," +
                    "       b.description," +
                    "       a.id AS author_id," +
                    "       a.first_name AS author_firstName," +
                    "       a.last_name AS author_lastName," +
                    "       c.category," +
                    "       c.description AS category_description " +
                    "FROM books b " +
                    "LEFT JOIN books_authors ba ON b.isbn = ba.book " +
                    "LEFT JOIN authors a ON ba.author = a.id " +
                    "LEFT JOIN categories_books cb ON b.isbn = cb.book " +
                    "LEFT JOIN categories c ON cb.category = c.id " +
                    "WHERE b.delete_at IS NULL " +
                    "AND b.isbn LIKE ? " +
                    "LIMIT 1";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            preparedStatement.setString(1, isbn);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                book = new Book();

                book.setBook(
                        resultSet.getString("isbn"),
                        resultSet.getInt("quantities"),
                        resultSet.getInt("pages"),
                        resultSet.getString("title"),
                        resultSet.getString("edition"),
                        Language.valueOf(resultSet.getString("language")),
                        resultSet.getString("description")
                );

                List<Author> authors = new ArrayList<>();
                List<Category> categories = new ArrayList<>();

                do {
                    int authorId = resultSet.getInt("author_id");
                    String authorFirstName = resultSet.getString("author_firstName");
                    String authorLastName = resultSet.getString("author_lastName");

                    if (authorId != 0 && authorFirstName != null && authorLastName != null) {
                        Author author = new Author();
                        author.setAuthor(
                                authorId,
                                authorFirstName,
                                authorLastName
                        );
                        authors.add(author);
                    }

                    String categoryStr = resultSet.getString("category");
                    String categoryDescription = resultSet.getString("category_description");

                    if (categoryStr != null) {
                        Category category = new Category();
                        category.setCategory(
                                categoryStr,
                                categoryDescription
                        );
                        categories.add(category);
                    }
                } while (resultSet.next());

                book.hasAuthors(authors);
                book.hasCategories(categories);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }

    public Book read() {
        Map<String, String> Book = super.read(new String[]{this.book.getIsbn()});

        System.out.println(Book.toString());

        this.book.setBook(
                Book.get("isbn"),
                Integer.parseInt(Book.get("quantities")),
                Integer.parseInt(Book.get("pages")),
                Book.get("title"),
                Book.get("edition"),
                Language.valueOf(Book.get("language")),
                Book.get("description")
        );

        return this.book;
    }

    private void executeAndWrrapQuery(List<Book> books, PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();

        String currentIsbn = null;
        Book currentBook = null;
        List<Author> authors = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        while (resultSet.next()) {
            String isbn = resultSet.getString("isbn");

            if (currentIsbn == null || !currentIsbn.equals(isbn)) {

                if (currentBook != null) {
                    currentBook.hasAuthors(authors);
                    currentBook.hasCategories(categories);
                    books.add(currentBook);
                }

                currentBook = new Book();
                currentBook.setBook(
                        isbn,
                        resultSet.getInt("quantities"),
                        resultSet.getInt("pages"),
                        resultSet.getString("title"),
                        resultSet.getString("edition"),
                        Language.valueOf(resultSet.getString("language")),
                        resultSet.getString("description")
                );

                authors = new ArrayList<>();
                categories = new ArrayList<>();
                currentIsbn = isbn;
            }

            int authorId = resultSet.getInt("author_id");
            String authorFirstName = resultSet.getString("author_firstName");
            String authorLastName = resultSet.getString("author_lastName");

            if (authorId != 0 && authorFirstName != null && authorLastName != null) {
                Author author = new Author();
                author.setAuthor(
                        authorId,
                        authorFirstName,
                        authorLastName
                );
                if(!authors.contains(author)){
                    authors.add(author);
                }
            }

            String categoryStr = resultSet.getString("category");
            String categoryDescription = resultSet.getString("category_description");

            if (categoryStr != null) {
                Category category = new Category();
                category.setCategory(
                        categoryStr,
                        categoryDescription
                );
                if(!categories.contains(category)){
                    categories.add(category);
                }
            }
        }

        if (currentBook != null) {
            currentBook.hasAuthors(authors);
            currentBook.hasCategories(categories);
            books.add(currentBook);
        }
    }


//    public List<Book> getAllBooksWhitAllDetail() {
//        List<Book> books = new ArrayList<>();
//
//        try {
//            String query = "SELECT DISTINCT" +
//                    "    b.isbn," +
//                    "    b.quantities," +
//                    "    b.pages," +
//                    "    b.title," +
//                    "    b.edition," +
//                    "    b.language," +
//                    "    b.description," +
//                    "    b.delete_at AS book_delete_at," +
//                    "    c.id AS category_id," +
//                    "    c.category AS category," +
//                    "    c.description AS category_description," +
//                    "    a.firstName AS author_firstName," +
//                    "    a.lastName AS author_lastName," +
//                    "    a.id AS author_id," +
//                    "    l.id AS loan_id," +
//                    "    l.user AS loan_user," +
//                    "    l.bookReference AS loan_bookReference," +
//                    "    l.loanDate AS loan_loanDate," +
//                    "    l.expectedReturnDate AS loan_expectedReturnDate," +
//                    "    l.returnDate AS loan_returnDate," +
//                    "    lo.loastDate AS lost_loastDate," +
//                    "    lo.lostCount AS lost_lostCount," +
//                    "    lo.description AS lost_description," +
//                    "    wl.id AS waitinglist_id," +
//                    "    wl.user AS waitinglist_user," +
//                    "    wl.loan AS waitinglist_loan," +
//                    "    wl.create_at AS waitinglist_create_at," +
//                    "    lg.id AS log_id," +
//                    "    lg.user AS log_user," +
//                    "    lg.create_at AS log_create_at " +
//                    "FROM" +
//                    "    books b" +
//                    "LEFT JOIN" +
//                    "    categories_books cb ON b.isbn = cb.book " +
//                    "LEFT JOIN" +
//                    "    categories c ON cb.category = c.id " +
//                    "LEFT JOIN" +
//                    "    books_authors ba ON b.isbn = ba.book " +
//                    "LEFT JOIN" +
//                    "    authors a ON ba.author = a.id " +
//                    "LEFT JOIN" +
//                    "    loans l ON b.isbn = l.book " +
//                    "LEFT JOIN" +
//                    "    losts lo ON b.isbn = lo.book " +
//                    "LEFT JOIN" +
//                    "    waitinglists wl ON b.isbn = wl.book " +
//                    "LEFT JOIN" +
//                    "    logs lg ON b.isbn = lg.book";
//
//            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            String currentIsbn = null;
//            Book currentBook = null;
//
//            List<Author> authors = new ArrayList<>();
//            List<Category> categories = new ArrayList<>();
//
//            List <Loan> loans = new ArrayList<>();
//            List<Lost> losts = new ArrayList<Lost>();
//
//            List<WaitingList> waitingLists = new ArrayList<WaitingList>();
//            List<Log> logs = new ArrayList<Log>();
//
//            while (resultSet.next()) {
//                String isbn = resultSet.getString("isbn");
//
//                if (currentIsbn == null || !currentIsbn.equals(isbn)) {
//
//                    if (currentBook != null) {
//                        currentBook.hasAuthors(authors);
//                        currentBook.hasCategories(categories);
//                        currentBook.setLoans(loans);
//                        currentBook.setLosts(losts);
//                        currentBook.setWaitingLists(waitingLists);
//                        currentBook.setLogs(logs);
//                        books.add(currentBook);
//                    }
//
//                    currentBook = new Book();
//                    currentBook.setBook(
//                            isbn,
//                            resultSet.getInt("quantities"),
//                            resultSet.getInt("pages"),
//                            resultSet.getString("title"),
//                            resultSet.getString("edition"),
//                            resultSet.getString("language"),
//                            resultSet.getString("description")
//                    );
//
//                    authors = new ArrayList<>();
//                    categories = new ArrayList<>();
//                    loans = new ArrayList<>();
//                    losts = new ArrayList<>();
//                    waitingLists = new ArrayList<>();
//                    logs = new ArrayList<>();
//                    currentIsbn = isbn;
//                }
//
//                int authorId = resultSet.getInt("author_id");
//                String authorFirstName = resultSet.getString("author_firstName");
//                String authorLastName = resultSet.getString("author_lastName");
//
//                if (authorId != 0 && authorFirstName != null && authorLastName != null) {
//                    Author author = new Author();
//                    author.setAuthor(
//                            authorId,
//                            authorFirstName,
//                            authorLastName
//                    );
//                    if(!authors.contains(author)){
//                        authors.add(author);
//                    }
//                }
//
//                String categoryStr = resultSet.getString("category");
//                String categoryDescription = resultSet.getString("category_description");
//
//                if (categoryStr != null) {
//                    Category category = new Category();
//                    category.setCategory(
//                            categoryStr,
//                            categoryDescription
//                    );
//                    if(!categories.contains(category)){
//                        categories.add(category);
//                    }
//                }
//
//                int loanId = resultSet.getInt("loan_id");
//                if (loanId != 0) {
//                    Loan loan = new Loan();
//                    User user = new User();
//                    user.setId(resultSet.getInt("loan_user"));
//                    try(UserDao userDao = new UserDao(user)){
//                        loan.setLoan(
//                                loanId,
//                                resultSet.getString("loan_bookReference"),
//                                resultSet.getTimestamp("loan_loanDate"),
//                                resultSet.getTimestamp("loan_returnDate"),
//                                resultSet.getTimestamp("loan_expectedReturnDate"),
//                                userDao.read()
//                        );
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                    loans.add(loan);
//                }
//
//                int lostId = resultSet.getInt("lost_id");
//                if (lostId != 0) {
//                    Lost lost = new Lost();
//                    lost.setLost(
//                            lostId,
//                            resultSet.getString("loan_bookReference"),
//                            resultSet.getDate("lost_loastDate"),
//                            resultSet.getString("lost_description"),
//                            LostStatus(resultSet.getString("status"))
//                    );
//                    losts.add(lost);
//                }
//
//                int waitingListId = resultSet.getInt("waitinglist_id");
//                if (waitingListId != 0) {
//                    WaitingList waitingList = new WaitingList();
//                    waitingList.setWaitingList(
//                            waitingListId,
//                            resultSet.getString("waitinglist_user"),
//                            resultSet.getInt("waitinglist_loan"),
//                            resultSet.getDate("waitinglist_create_at")
//                    );
//                    waitingLists.add(waitingList);
//                }
//
//                int logId = resultSet.getInt("log_id");
//                if (logId != 0) {
//                    Log log = new Log();
//                    log.setLog(
//                            logId,
//                            resultSet.getString("log_user"),
//                            resultSet.getDate("log_create_at")
//                    );
//                    logs.add(log);
//                }
//            }
//
//            if (currentBook != null) {
//                currentBook.hasAuthors(authors);
//                currentBook.hasCategories(categories);
//                currentBook.setLoans(loans);
//                currentBook.setLosts(losts);
//                currentBook.setWaitingLists(waitingLists);
//                currentBook.setLogs(logs);
//                books.add(currentBook);
//            }
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return books;
//    }


    public Book getBookWithDetails() {
        boolean flag = true;
        try {
            String query = "SELECT b.isbn, b.quantities, b.pages, b.title, b.edition, b.language, b.description,a.id, a.first_name, a.last_name, c.category, c.description c_description " +
                    "FROM books b " +
                    "LEFT JOIN books_authors ba ON b.isbn = ba.book " +
                    "LEFT JOIN authors a ON ba.author = a.id " +
                    "LEFT JOIN categories_books cb ON b.isbn = cb.book " +
                    "LEFT JOIN categories c ON cb.category = c.id " +
                    "WHERE b.isbn = ? AND b.delete_at IS NULL ";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, this.book.getIsbn());

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Author> authors = new ArrayList<>();
            List<Category> categories = new ArrayList<>();

            while (resultSet.next()) {
                if(flag){
                    this.book.setBook(
                            resultSet.getString("isbn"),
                            resultSet.getInt("quantities"),
                            resultSet.getInt("pages"),
                            resultSet.getString("title"),
                            resultSet.getString("edition"),
                            Language.valueOf(resultSet.getString("language")),
                            resultSet.getString("description")
                    );
                    flag = false;
                }

                int authorId = resultSet.getInt("id");
                String authorFirstName = resultSet.getString("first_name");
                String authorLastName = resultSet.getString("last_name");

                if (authorFirstName != null && authorLastName != null && authorId != 0) {
                    Author author =  new Author();
                    author.setAuthor(
                            authorId,
                            authorFirstName,
                            authorLastName
                    );
                    authors.add(author);
                }

                int categoryId = resultSet.getInt("id");
                String categoryStr = resultSet.getString("category");
                String categoryDescription = resultSet.getString("c_description");

                if (categoryStr != null) {
                    Category category = new Category();
                    category.setCategory(
                            categoryId,
                            categoryStr,
                            categoryDescription
                    );
                    categories.add(category);
                }
            }

            this.book.hasAuthors(authors);
            this.book.hasCategories(categories);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.book;
    }

    public boolean save() throws SQLException {
        this.book.setIsbn(this.create(this.book.getBook()) );
        return this.book.getIsbn() != null;
    }


}
