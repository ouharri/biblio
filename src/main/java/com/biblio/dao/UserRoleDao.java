package com.biblio.dao;

import com.biblio.libs.Model;

public class UserRoleDao extends Model {
    public UserRoleDao() {
        super("users_roles", new String[]{"user", "role"});
    }

}
