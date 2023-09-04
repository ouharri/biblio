package com.biblio.core;

import com.biblio.helpers.envLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class database {
    private static Connection connection = null;
    private static final envLoader env = new envLoader();

   static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbUrl = env.get("DB_URL");
            String dbUsername = env.get("DB_USERNAME");
            String dbPassword = env.get("DB_PASSWORD");
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
   }



    public static Connection getConnection() {
        return connection;
    }



}

