package com.biblio.dao;

import com.biblio.libs.db;

public class BooksAuthorsDao extends db {


    public BooksAuthorsDao() {
        super("books_authors",new String[]{"id"});
        this._softDelete = false;
    }
}
