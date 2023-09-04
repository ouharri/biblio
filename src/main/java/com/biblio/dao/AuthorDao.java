package com.biblio.dao;

import com.biblio.libs.db;

public class AuthorDao extends db {

    public AuthorDao() {
        super("authors", new String[]{"id"});
    }
}
