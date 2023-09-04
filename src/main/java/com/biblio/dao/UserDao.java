package com.biblio.dao;

import com.biblio.libs.db;

public class UserDao extends db {
    public UserDao() {
        super("users", new String[]{"id"});
    }
}
