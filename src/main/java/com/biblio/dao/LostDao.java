package com.biblio.dao;

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

//    public boolean bookAlreadyLost(String isbn,String book_reference){
//
//    }


}
