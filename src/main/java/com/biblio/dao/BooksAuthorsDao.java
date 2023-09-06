package com.biblio.dao;

import com.biblio.libs.Model;

public class BooksAuthorsDao extends Model {


    public BooksAuthorsDao() {
        super("books_authors",new String[]{"id"});
        this._softDelete = false;
    }
}
