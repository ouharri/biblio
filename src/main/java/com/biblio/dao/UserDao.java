package com.biblio.dao;

import com.biblio.app.model.User;

import java.sql.SQLException;

public class UserDao extends User {


    public boolean create() throws SQLException {
        this.id = Integer.parseInt(super.create(this.getUser()));
        return this.id > 0;
    }

}
