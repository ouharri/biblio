package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class Log {

	private Connection connection;

	public Log() {
		this.connection = database.getConnection();
	}

	public int id;
	public User user;
	public Book book;

	private Log select() {
		return null;
	}

	private Log add() {
		return null;
	}

	private Log update() {
		return null;
	}

	private boolean delete() {
		return false;
	}

}
