package com.biblio.dao;

import com.biblio.app.Models.*;
import com.biblio.libs.Model;

import java.sql.*;
import java.util.Map;

public final class LoanDao extends Model {

    public LoanDao() throws SQLException {
        super("borrowed_books", new String[]{"book","user","book_reference","request_at"});
        this._softDelete = false;
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

                                        try(BookDao bookDao = new BookDao()){
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
                        "SELECT  EXISTS(MAX(requested_at)) AS loan_exists " +
                                "FROM borrowed_books " +
                                "WHERE book = ? AND user = ? AND book_reference = ? AND return_date IS NULL"
                )
        ) {
            selectStatement.setString(1, isbn);
            selectStatement.setString(2, cnie);
            selectStatement.setString(3, book_reference);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("loan_exists");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
