package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class Statuses {

	private Connection connection;

	public Statuses() {
		this.connection = database.getConnection();
	}

	private int id;
	private int attribute31;
	private int status;

}
