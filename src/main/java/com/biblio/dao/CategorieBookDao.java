package com.biblio.dao;

import com.biblio.libs.db;

public class CategorieBookDao extends db {
    public CategorieBookDao() {
        super("categoriesbook", new String[]{"book","category"});
    }
}