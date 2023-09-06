package com.biblio.dao;

import com.biblio.libs.Model;

public final class CategorieBookDao extends Model {
    public CategorieBookDao() {
        super("categories_books",new String[]{"id"});
        this._softDelete = false;
    }
}
