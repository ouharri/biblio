package com.biblio.dao;

import com.biblio.app.Models.Author;
import com.biblio.app.Models.Book;
import com.biblio.app.Models.Log;
import com.biblio.libs.Model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class AuthorDao extends Model {

    private Author author = null;


    public AuthorDao() {
        super("authors", new String[]{"id"});
        this.author = new Author();
    }

    public List<Author> getAuthorByBook(Book book) throws Exception {

        try(BooksAuthorsDao BooksAuthorsDao = new BooksAuthorsDao()){
            List<Map<String, String>> resultList = BooksAuthorsDao.readAll("book",book.getIsbn());
            List<Author> authors = new ArrayList<Author>();

            for (Map<String, String> rowData : resultList) {
                this.author.setId(Integer.parseInt(rowData.get("id")));
                this.author.setFirst_name(rowData.get("first_name"));
                this.author.setLast_name(rowData.get("last_name"));
                authors.add(this.author);
            }
            return authors;
        }
    }

    public String[] getAllAuthors() {
        List<Map<String, String>> resultList = this.getAll();
        String[] authors = new String[resultList.size()];

        for (int i = 0; i < resultList.size(); i++) {
            Map<String, String> rowData = resultList.get(i);
            int id = Integer.parseInt(rowData.get("id"));
            String fullName = id + " ," + rowData.get("first_name") + " " + rowData.get("last_name");

            authors[i] = String.valueOf(fullName);
        }

        return authors;
    }



}
