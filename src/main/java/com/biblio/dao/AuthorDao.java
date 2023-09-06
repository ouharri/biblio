package com.biblio.dao;

import com.biblio.app.model.Author;
import com.biblio.app.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public final class AuthorDao extends Author {

    public Author[] getAuthorByBook(Book book) throws Exception {

        try(BooksAuthorsDao BooksAuthorsDao = new BooksAuthorsDao()){
            List<Map<String, String>> resultList = BooksAuthorsDao.readAll("book",book.getIsbn());
            Author[] authors = new Author[resultList.size()];

            for (int i = 0; i < resultList.size(); i++) {
                Map<String, String> rowData = resultList.get(i);
                    this.setId(Integer.parseInt(rowData.get("id")));
                this.setFirstName(rowData.get("firstName"));
                this.setLastName(rowData.get("lastName"));
                    authors[i] = this;
            }
            return authors;
        }
    }

    @Override
    public boolean create() throws SQLException {
        return false;
    }

    @Override
    public Book read() {
        return null;
    }

    @Override
    public boolean update() throws SQLException {
        return false;
    }

    @Override
    public boolean delete() {
        return false;
    }
}
