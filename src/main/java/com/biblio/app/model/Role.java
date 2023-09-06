package com.biblio.app.model;

import com.biblio.libs.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Role extends Model {
    public Role() {
        super("roles", new String[]{"id"});
    }

    protected int id;
    protected String role;

    protected List<User> users = new ArrayList<User>();

    public void setRole(int id, String roleTitle) {
        this.id = id;
        this.role = roleTitle;
    }

    public void hasUsers(List<User> users) {
        this.users = users;
    }
}
