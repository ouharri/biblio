package com.biblio.app.model;

import com.biblio.libs.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Role extends Model {
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
