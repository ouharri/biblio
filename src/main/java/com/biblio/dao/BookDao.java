package com.biblio.dao;

import com.biblio.libs.db;
import com.biblio.model.Author;
import com.biblio.model.Book;
import com.biblio.model.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BookDao extends db {
    public BookDao() {
        super("books",new String[]{"isbn"});
    }


    public abstract boolean create() throws SQLException;

    public abstract Book read();

    public abstract boolean delete();

    public abstract boolean update() throws SQLException;

//    SELECT b.isbn, b.quantities, b.pages, b.title, b.edition, b.language, b.description, a.firstName, a.lastName, c.category FROM books b LEFT JOIN books_authors ba ON b.isbn = ba.book LEFT JOIN authors a ON ba.author = a.id LEFT JOIN categories_books cb ON b.isbn = cb.book LEFT JOIN categories c ON cb.category = c.id WHERE b.isbn = 40;



}
