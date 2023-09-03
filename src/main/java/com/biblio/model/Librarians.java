package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class Librarians extends Users {

    private Connection connection;

    public Librarians() {
        this.connection = database.getConnection();
    }

}
