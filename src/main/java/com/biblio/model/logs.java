package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class logs {

	private Connection connection;

	public logs() {
		this.connection = database.getConnection();
	}

	public int id;
	public users user;
	public books book;

	private logs select() {
		return null;
	}

	private logs add() {
		return null;
	}

	private logs update() {
		return null;
	}

	private boolean delete() {
		return false;
	}

}
