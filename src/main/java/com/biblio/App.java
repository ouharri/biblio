package com.biblio;

import com.biblio.app.Controllers.AuthenticationController;
import com.biblio.app.Enums.Gender;
import com.biblio.core.database;
import com.biblio.view.Authentication.Signing;

import java.sql.Connection;
import java.sql.SQLException;

public class App implements AutoCloseable{

    private Connection connection = null;

    public App() {
        try {
            this.connection = database.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        687674987398749
    }

    public static void main(String[] args) throws Exception {
        new Signing();
    }

    @Override
    public void close() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}