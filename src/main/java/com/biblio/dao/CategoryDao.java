package com.biblio.dao;

import com.biblio.libs.db;

public class CategoryDao extends db {
    public CategoryDao() {
        super("categories", new String[]{"id"});
    }
}
