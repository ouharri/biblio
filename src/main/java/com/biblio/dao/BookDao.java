package com.biblio.dao;

import com.biblio.libs.db;

public class BookDao extends db {
    public BookDao() {
        super("books",new String[]{"isbn"});
    }

}
