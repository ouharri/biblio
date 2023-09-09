package com.biblio.dao;

import com.biblio.app.Models.Role;
import com.biblio.libs.Model;

public final class RoleDao extends Model {

    public RoleDao() {
        super("roles", new String[]{"id"});
    }

}
