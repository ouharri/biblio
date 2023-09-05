package com.biblio.dao;

import com.biblio.app.model.Role;
import com.biblio.app.model.User;
import com.biblio.libs.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserRoleDao extends db {
    public UserRoleDao() {
        super("users_roles", new String[]{"user", "role"});
    }

}
