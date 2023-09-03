package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class Role {

	private Connection connection;

	public Role() {
		this.connection = database.getConnection();
	}

	private int id;
	private String role;

}
