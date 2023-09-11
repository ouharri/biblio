package com.biblio.app.Controllers;

import com.biblio.app.Enums.LostStatus;
import com.biblio.app.Exceptions.NoQuantityBookException;
import com.biblio.app.Models.Book;
import com.biblio.dao.*;
import com.biblio.app.Enums.Language;

import java.sql.SQLException;
import java.util.List;

public class BookController {

    private final BookDao bookDao;
    private final LoanDao loanDao;
    private final LostDao lostDao;


    /**
     * Constructs a new BookController.
     *
     * @throws SQLException If a database error occurs.
     */
    public BookController() throws SQLException {
        bookDao = new BookDao();
        loanDao = new LoanDao();
        lostDao = new LostDao();
    }

    /**
     * Retrieves a list of all books.
     *
     * @return A list of books.
     */
    public List<Book> getAlBooks() {
        return bookDao.getAllBooks();
    }
    /**
     * Retrieves a list of all available books.
     *
     * @return A list of available books.
     */
    public List<Book> getAvailableBooks() { return bookDao.getAvailableBooks(); }
    /**
     * Retrieves a list of all lost books.
     *
     * @return A list of lost books.
     */
    public List<Book> getLostBooks() {
        return bookDao.getLostBooks();
    }
    /**
     * Retrieves a list of all borrowed books.
     *
     * @return A list of borrowed books.
     */
    public List<Book> getBorrowedBooks() {
        return bookDao.getBorrowedBooks();
    }
    /**
     * Retrieves a list of all borrowed books by a user.
     *
     * @param cnie The CNIE (user identifier) of the user.
     * @return A list of borrowed books.
     */
    public Book find(String isbn) throws SQLException {
        return bookDao.find(isbn);
    }
    /**
     * Adds a new book to the database.
     *
     * @param isbn         The ISBN of the book.
     * @param title        The title of the book.
     * @param description  The description of the book.
     * @param lang         The language of the book.
     * @param quantity     The quantity of copies available.
     * @param pages        The number of pages in the book.
     * @param edition      The edition of the book.
     * @param author_id    An array of author IDs associated with the book.
     * @param category_id  An array of category IDs associated with the book.
     * @return The newly added book.
     * @throws SQLException If a database error occurs.
     */
    public Book addBook(String isbn , String title, String description,Language lang,int quantity,int pages,String edition, int[] author_id, int[] category_id) throws SQLException {
        return bookDao.insert(
                isbn ,
                title,
                description,
                lang,
                quantity,
                pages,
                edition,
                author_id,
                category_id
        );
    }
    /**
     * Updates an existing book in the database.
     *
     * @param isbn         The ISBN of the book.
     * @param title        The updated title of the book.
     * @param description  The updated description of the book.
     * @param lang         The updated language of the book.
     * @param quantity     The updated quantity of copies available.
     * @param pages        The updated number of pages in the book.
     * @param edition      The updated edition of the book.
     * @param author_id    The updated array of author IDs associated with the book.
     * @param category_id  The updated array of category IDs associated with the book.
     * @return The updated book.
     * @throws SQLException If a database error occurs.
     */
    public Book updateBook(String isbn , String title, String description,Language lang,int quantity,int pages,String edition, int[] author_id, int[] category_id) throws SQLException {
        return bookDao.update(
                isbn ,
                title,
                description,
                lang,
                quantity,
                pages,
                edition,
                author_id,
                category_id
        );
    }
    /**
     * Deletes a book from the database by ISBN.
     *
     * @param isbn The ISBN of the book to delete.
     * @return `true` if the book was successfully deleted, `false` otherwise.
     * @throws SQLException If a database error occurs.
     */
    public boolean deleteBook(String isbn) throws SQLException {
        return bookDao.delete(isbn);
    }
    /**
     * Searches for books by keyword.
     *
     * @param Keyword The keyword to search for.
     * @return A list of books matching the keyword.
     * @throws SQLException If a database error occurs.
     */
    public List<Book> searchBook(String Keyword) throws SQLException {
        return bookDao.search(Keyword);
    }

    /**
     * Searches for available books by keyword.
     *
     * @param Keyword The keyword to search for.
     * @return A list of available books matching the keyword.
     * @throws SQLException If a database error occurs.
     */
    public List<Book> searchInAvailableBooks(String Keyword) throws SQLException {
        return bookDao.searchInAvailableBooks(Keyword);
    }

    /**
     * Loans a book if copies are available.
     *
     * @param isbn                The ISBN of the book to loan.
     * @param book_reference      A reference for the borrowed book.
     * @param cnie                The CNIE (user identifier) of the borrower.
     * @param loan_date           The loan date.
     * @param expected_return_date The expected return date.
     * @return `true` if the book was successfully loaned, `false` otherwise.
     * @throws Exception If an error occurs during loan processing.
     */
    public boolean loanBook(String isbn,String book_reference,String cnie,java.sql.Timestamp loan_date,java.sql.Timestamp expected_return_date) throws Exception {
        if(bookDao.existsBookQuantity(isbn) && !loanDao.userAlreadyLoanBook(isbn, cnie, book_reference)){
            return loanDao.loanBook(isbn,book_reference,cnie,loan_date,expected_return_date) != null;
        }
        throw new NoQuantityBookException();
    }
    /**
     * Returns a borrowed book by ISBN, CNIE, and book reference.
     *
     * @param isbn          The ISBN of the book to return.
     * @param cnie          The CNIE (user identifier) of the borrower.
     * @param book_reference The reference of the borrowed book.
     * @return `true` if the book was successfully returned, `false` otherwise.
     * @throws Exception If an error occurs during return processing.
     */
    public boolean returnBook(String isbn, String cnie, String book_reference) throws Exception {
        if(loanDao.userAlreadyLoanBook(isbn, cnie, book_reference)) {
            return this.loanDao.returnBook(isbn, cnie, book_reference) != null;
        }

        throw new NoQuantityBookException();
    }


    /**
     * Registers a lost book in the database.
     *
     * @param isbn         The ISBN of the lost book.
     * @param book_reference The reference of the lost book.
     * @param description  A description of the loss.
     * @return `true` if the book was successfully registered as lost, `false` otherwise.
     */
    public boolean lostBook(String isbn,String book_reference,String description) throws Exception {
        return lostDao.add(isbn,book_reference, description) != null;
    }

    /**
     * Updates a lost book in the database.
     *
     * @param isbn         The ISBN of the lost book.
     * @param book_reference The reference of the lost book.
     * @param description  A description of the loss.
     * @param status       The status of the lost book.
     * @param requested_ad The date the loss was reported.
     * @return `true` if the book was successfully updated, `false` otherwise.
     */
    public boolean updateLost(String isbn,String book_reference,String description, LostStatus status,java.sql.Timestamp requested_ad) throws Exception {
        return lostDao.updateLost(isbn,book_reference, description, status,requested_ad) != null;
    }

}