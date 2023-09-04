package com.biblio.dao;

import com.biblio.libs.db;

public class LogDao extends db {
    public LogDao() {
        super("logs", new String[]{"id"});
    }
}
