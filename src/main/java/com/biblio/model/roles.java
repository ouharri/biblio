package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class roles {

	private Connection connection;

	public roles() {
		this.connection = database.getConnection();
	}

	private int id;
	private String role;

}
