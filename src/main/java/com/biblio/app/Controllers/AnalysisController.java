package com.biblio.app.Controllers;

import com.biblio.app.Enums.LostStatus;
import com.biblio.dao.BookDao;
import com.biblio.dao.LoanDao;
import com.biblio.dao.LostDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnalysisController {
    private final BookDao bookDao;
    private final LoanDao loanDao;
    private final LostDao lostDao;


    public AnalysisController() throws SQLException {
        bookDao = new BookDao();
        loanDao = new LoanDao();
        lostDao = new LostDao();
    }
    /**
     * Gets the total number of books in the database.
     *
     * @return The total number of books.
     */
    public int getTotalBooks() {
        return bookDao.getTotalBooks();
    }

    /**
     * Gets the total number of available books in the database.
     *
     * @return The total number of available books.
     */
    public int getTotalAvailableBooks() {
        return bookDao.getTotalAvailableBooks();
    }

    /**
     * Gets the total number of borrowed books in the database.
     *
     * @return The total number of borrowed books.
     */
    public int getTotalBorrowedBooks() {
        return bookDao.getTotalBorrowedBooks();
    }

    /**
     * Gets the total number of lost books in the database.
     *
     * @return The total number of lost books.
     */
    public int getTotalLostBooks() {
        return bookDao.getTotalLostBooks();
    }

    /**
     * Gets the total number of borrowed books that have not been returned in the database.
     *
     * @return The total number of borrowed books that have not been returned.
     */
    public int getTotalBorrowedNotReturnedBooks(){
        return bookDao.getTotalBorrowedNotReturnedBooks();
    }

    /**
     * Gets the total stock of books in the database.
     *
     * @return The total stock of books.
     */
    public int getTotalStock() {
        return bookDao.getTotalStock();
    }

    /**
     * Gets the total number of returned books in the database.
     *
     * @return The total number of returned books.
     */
    public int getTotalReturnedBooks() {
        return bookDao.getTotalReturnedBooks();
    }

    /**
     * Gets the total number of authors in the database.
     *
     * @return The total number of authors.
     */
    public int getTotalAuthors() {
        return bookDao.getTotalAuthors();
    }

    /**
     * Gets the total number of categories in the database.
     *
     * @return The total number of categories.
     */
    public int getTotalCategories() {
        return bookDao.getTotalCategories();
    }

    /**
     * Gets the total number of users who have borrowed books in the database.
     *
     * @return The total number of users with borrowed books.
     */
    public int getTotalBorrowedUsers() {
        return bookDao.getTotalBorrowedUsers();
    }

    /**
     * Gets the total number of users who have not borrowed any books in the database.
     *
     * @return The total number of users with no borrowed books.
     */
    public int getTotalAvailableUsers() {
        return bookDao.getTotalAvailableUsers();
    }

    /**
     * Gets the total number of users in the database.
     *
     * @return The total number of users.
     */

    public int getTotalUsers() {
        return bookDao.getTotalUsers();
    }
}
