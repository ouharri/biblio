package com.biblio.dao;

import com.biblio.libs.db;

public class LostDao extends db {
    public LostDao() {
        super("losts", new String[]{"id"});
    }
}
