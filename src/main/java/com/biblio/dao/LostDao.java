package com.biblio.dao;

import com.biblio.app.Enums.LostStatus;
import com.biblio.app.Models.Book;
import com.biblio.app.Models.Lost;
import com.biblio.libs.Model;

import java.util.Map;

public final class LostDao extends Model {


    public LostDao() {
        super("lost_books", new String[]{"book","book_reference","lost_date"});
    }

    public Lost add(String isbn,String book_reference,String description) throws Exception {

        Book book;
        Lost lost = new Lost();

        try (BookDao bookDao = new BookDao()) {
            book = bookDao.find(isbn);
        }

        lost.setBook(
                book.getIsbn(),
                book.getQuantities(),
                book.getPages(),
                book.getTitle(),
                book.getEdition(),
                book.getLanguage(),
                book.getDescription()
        );

        lost.setLost(
                book_reference,
                description
        );

        Map<String, String> LostData = lost.getLost();

        if (super.create(LostData) != null) {
            return lost;
        }

        return null;

    }

    public Lost updateLost(String isbn, String book_reference,String description, LostStatus status,java.sql.Timestamp requested_ad) throws Exception {

        Book book;
        Lost lost = new Lost();

        try (BookDao bookDao = new BookDao()) {
            book = bookDao.find(isbn);
        }

        lost.setBook(
                book.getIsbn(),
                book.getQuantities(),
                book.getPages(),
                book.getTitle(),
                book.getEdition(),
                book.getLanguage(),
                book.getDescription()
        );

        lost.setLost(
                book_reference,
                description,
                status
        );

        Map<String, String> LostData = lost.getLost();

        if (super.update(LostData, new String[]{isbn, book_reference, String.valueOf(requested_ad)})) {
            return lost;
        }

        return null;

    }

}
