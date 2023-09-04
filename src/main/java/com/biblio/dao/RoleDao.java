package com.biblio.dao;

import com.biblio.libs.db;

public class RoleDao extends db {

        public RoleDao() {
            super("roles", new String[]{"id"});
        }
}
