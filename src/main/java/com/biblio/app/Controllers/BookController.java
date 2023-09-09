package com.biblio.app.Controllers;

import com.biblio.app.Models.Book;
import com.biblio.dao.AuthorDao;
import com.biblio.dao.BookDao;
import com.biblio.dao.CategoryDao;
import com.biblio.app.Enums.Language;

import java.sql.SQLException;
import java.util.List;

public class BookController {
    private final BookDao bookDao = new BookDao();
    private final CategoryDao categoryDao = new CategoryDao();
    private final AuthorDao authorDao = new AuthorDao();
    public List<Book> getBooks() {
        return bookDao.getAllBooks();
    }
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
    public boolean deleteBook(String isbn) throws SQLException {
        return bookDao.delete(isbn);
    }
    public List<Book> searchBook(String Keyword) throws SQLException {
        return bookDao.search(Keyword);
    }
}