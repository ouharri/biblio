package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class members extends users {

	private Connection connection;

	public members() {
		this.connection = database.getConnection();
	}

	public void borrow() {

	}

}
