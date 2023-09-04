package com.biblio.dao;

import com.biblio.libs.db;

public class WaitingListDao extends db {
    public WaitingListDao() {
        super("waitinglists", new String[]{"id"});
    }
}
