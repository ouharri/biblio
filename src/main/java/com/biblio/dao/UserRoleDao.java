package com.biblio.dao;

import com.biblio.libs.Model;

public final class UserRoleDao extends Model {
    public UserRoleDao() {
        super("users_roles", new String[]{"user", "role"});
    }

}
