package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class librarians extends users {

    private Connection connection;

    public librarians() {
        this.connection = database.getConnection();
    }

}
