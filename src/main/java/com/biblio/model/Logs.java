package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class Logs {

	private Connection connection;

	public Logs() {
		this.connection = database.getConnection();
	}

	public int id;
	public Users user;
	public Books book;

	private Logs select() {
		return null;
	}

	private Logs add() {
		return null;
	}

	private Logs update() {
		return null;
	}

	private boolean delete() {
		return false;
	}

}
