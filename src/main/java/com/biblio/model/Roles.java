package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class Roles {

	private Connection connection;

	public Roles() {
		this.connection = database.getConnection();
	}

	private int id;
	private String role;

}
