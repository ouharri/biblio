package com.biblio.app.model;

import com.biblio.libs.db;
public abstract class Role extends db{
    public Role() {
        super("roles", new String[]{"id"});
    }

    int id;
    String role;

    public void setRole(int id, String roleTitle) {
        this.id = id;
        this.role = roleTitle;
    }
}
