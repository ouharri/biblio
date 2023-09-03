package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class Members extends Users {

	private Connection connection;

	public Members() {
		this.connection = database.getConnection();
	}

	public void borrow() {

	}

}
