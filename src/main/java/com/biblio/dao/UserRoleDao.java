package com.biblio.dao;

import com.biblio.libs.db;

public class UserRoleDao extends db {
    public UserRoleDao() {
        super("user_roles", new String[]{"user", "role"});
    }
}
