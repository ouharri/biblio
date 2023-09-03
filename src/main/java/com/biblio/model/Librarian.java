package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class Librarian extends User {

    private Connection connection;

    public Librarian() {
        this.connection = database.getConnection();
    }

}
