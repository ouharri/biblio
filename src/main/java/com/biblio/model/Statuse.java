package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class Statuse {

	private Connection connection;

	public Statuse() {
		this.connection = database.getConnection();
	}

	private int id;
	private int attribute31;
	private int status;

}
