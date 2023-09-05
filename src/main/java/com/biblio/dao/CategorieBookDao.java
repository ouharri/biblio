package com.biblio.dao;

import com.biblio.libs.db;

public class CategorieBookDao extends db {
    public CategorieBookDao() {
        super("categories_books",new String[]{"id"});
        this._softDelete = false;
    }
}
