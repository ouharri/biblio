package com.biblio.dao;

import com.biblio.app.Enums.Language;
import com.biblio.app.Models.*;
import com.biblio.libs.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class LoanDao extends Model {

    public LoanDao() throws SQLException {
        super("borrowed_books", new String[]{"book", "user", "book_reference", "request_at"});
        this._softDelete = false;
    }

    public List<Loan> getBorrowedBooks() {
        List<Loan> borrowedBooks = new ArrayList<>();

        try {
            String query = "SELECT DISTINCT " +
                    "    b.isbn, " +
                    "    b.quantities, " +
                    "    b.pages, " +
                    "    b.title, " +
                    "    b.edition, " +
                    "    b.language, " +
                    "    b.description, " +
                    "    a.id AS author_id, " +
                    "    a.first_name AS author_firstName, " +
                    "    a.last_name AS author_lastName, " +
                    "    c.category, " +
                    "    c.description AS category_description, " +
                    "    bb.book_reference, " +
                    "    bb.loan_date, " +
                    "    bb.expected_return_date, " +
                    "    bb.return_date, " +
                    "    bb.requested_at, " +
                    "    u.cnie, " +
                    "    u.first_name, " +
                    "    u.last_name " +
                    "FROM books b " +
                    "LEFT JOIN books_authors ba ON b.isbn = ba.book " +
                    "LEFT JOIN authors a ON ba.author = a.id " +
                    "LEFT JOIN categories_books cb ON b.isbn = cb.book " +
                    "LEFT JOIN categories c ON cb.category = c.id " +
                    "INNER JOIN borrowed_books bb ON b.isbn = bb.book " +
                    "INNER JOIN users u ON bb.user = u.cnie " +
                    "WHERE b.delete_at IS NULL " +
                    "ORDER BY bb.return_date;";

            PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            String currentIsbn = null;
            Loan currentBook = null;
            List<Author> authors = new ArrayList<>();
            List<Category> categories = new ArrayList<>();
            User user = null;

            while (resultSet.next()) {
                String isbn = resultSet.getString("isbn");

                if (currentIsbn == null || !currentIsbn.equals(isbn)) {

                    if (currentBook != null) {
                        currentBook.hasAuthors(authors);
                        currentBook.hasCategories(categories);
                        borrowedBooks.add(currentBook);
                    }

                    currentBook = new Loan();
                    currentBook.setBook(
                            isbn,
                            resultSet.getInt("quantities"),
                            resultSet.getInt("pages"),
                            resultSet.getString("title"),
                            resultSet.getString("edition"),
                            Language.valueOf(resultSet.getString("language")),
                            resultSet.getString("description")
                    );
                    currentBook.setLoan(
                            null,
                            resultSet.getString("book_reference"),
                            resultSet.getTimestamp("loan_date"),
                            resultSet.getTimestamp("return_date"),
                            resultSet.getTimestamp("expected_return_date"),
                            resultSet.getTimestamp("requested_at")
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
                    if (!authors.contains(author)) {
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
                    if (!categories.contains(category)) {
                        categories.add(category);
                    }
                }

                String cnie = resultSet.getString("cnie");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");

                user = null;

                if (cnie != null) {
                    user = new User();
                    user.setUser(
                            cnie,
                            first_name,
                            last_name
                    );
                }
            }

            if (currentBook != null) {
                currentBook.hasAuthors(authors);
                currentBook.hasCategories(categories);
                if (user != null) {
                    currentBook.hasUser(user);
                }
                borrowedBooks.add(currentBook);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return borrowedBooks;
    }

    public Loan loanBook(String isbn, String book_reference, String cnie, java.sql.Timestamp loan_date, java.sql.Timestamp expected_return_date) throws Exception {

        User user;
        Book book;

        Loan loan = new Loan();

        try (UserDao userDao = new UserDao()) {
            user = userDao.find(cnie);
        }

        try (BookDao bookDao = new BookDao()) {
            book = bookDao.find(isbn);
        }

        loan.setBook(
                book.getIsbn(),
                book.getQuantities(),
                book.getPages(),
                book.getTitle(),
                book.getEdition(),
                book.getLanguage(),
                book.getDescription()
        );

        loan.setLoan(
                user,
                book_reference,
                loan_date,
                expected_return_date
        );

        Map<String, String> LoanData = loan.getLoan();

        if (super.create(LoanData) != null) {
            return loan;
        }

        return null;
    }

    public Loan returnBook(String isbn, String cnie, String book_reference) throws Exception {
        Loan loan = null;

        try (
                PreparedStatement selectStatement = this.connection.prepareStatement(
                        "SELECT MAX(requested_at) AS most_recent_request " +
                                "FROM borrowed_books " +
                                "WHERE book = ? AND user = ? AND book_reference = ? AND return_date IS NULL"
                )
        ) {
            selectStatement.setString(1, isbn);
            selectStatement.setString(2, cnie);
            selectStatement.setString(3, book_reference);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {

                    java.sql.Timestamp mostRecentRequest = resultSet.getTimestamp("most_recent_request");

                    try (
                            PreparedStatement updateStatement = this.connection.prepareStatement(
                                    "UPDATE borrowed_books " +
                                            "SET return_date = CURRENT_TIMESTAMP() " +
                                            "WHERE book = ? AND user = ? AND book_reference = ? " +
                                            "AND requested_at = ?"
                            )
                    ) {
                        updateStatement.setString(1, isbn);
                        updateStatement.setString(2, cnie);
                        updateStatement.setString(3, book_reference);
                        updateStatement.setTimestamp(4, mostRecentRequest);

                        int rowsAffected = updateStatement.executeUpdate();

                        if (rowsAffected == 1) {

                            try (
                                    PreparedStatement selectUpdatedLoan = this.connection.prepareStatement(
                                            "SELECT * FROM borrowed_books WHERE book = ? AND user = ? AND book_reference = ? AND requested_at = ?"
                                    )
                            ) {
                                selectUpdatedLoan.setString(1, isbn);
                                selectUpdatedLoan.setString(2, cnie);
                                selectUpdatedLoan.setString(3, book_reference);
                                selectUpdatedLoan.setTimestamp(4, mostRecentRequest);

                                try (ResultSet updatedLoanResultSet = selectUpdatedLoan.executeQuery()) {
                                    if (updatedLoanResultSet.next()) {

                                        loan = new Loan();

                                        Book book = new Book();
                                        User user = new User();

                                        try (BookDao bookDao = new BookDao()) {
                                            book = bookDao.find(isbn);
                                        }

                                        try (UserDao userDao = new UserDao()) {
                                            user = userDao.find(cnie);
                                        }

                                        loan.setBook(
                                                book.getIsbn(),
                                                book.getQuantities(),
                                                book.getPages(),
                                                book.getTitle(),
                                                book.getEdition(),
                                                book.getLanguage(),
                                                book.getDescription()
                                        );

                                        loan.setLoan(
                                                user,
                                                updatedLoanResultSet.getString("book_reference"),
                                                updatedLoanResultSet.getTimestamp("loan_date"),
                                                updatedLoanResultSet.getTimestamp("return_date"),
                                                updatedLoanResultSet.getTimestamp("expected_return_date"),
                                                updatedLoanResultSet.getTimestamp("requested_at")
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loan;
    }

    public boolean userAlreadyLoanBook(String isbn, String cnie, String book_reference) throws SQLException {
        try (
                PreparedStatement selectStatement = this.connection.prepareStatement(
                        "SELECT COUNT(*) AS loan_exists " +
                                "FROM borrowed_books " +
                                "WHERE book = ? AND user = ? AND book_reference = ? AND return_date IS NULL " +
                                "LIMIT 1"
                )
        ) {
            selectStatement.setString(1, isbn);
            selectStatement.setString(2, cnie);
            selectStatement.setString(3, book_reference);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("loan_exists") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
