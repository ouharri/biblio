package com.biblio.dao;

import com.biblio.libs.db;

public class LoanDao extends db {
    public LoanDao() {
        super("loans", new String[]{"id"});
    }
}
