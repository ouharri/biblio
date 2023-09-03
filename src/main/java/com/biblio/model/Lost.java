package com.biblio.model;

import java.util.Date;
import java.sql.Connection;
import com.biblio.core.database;


public class Lost {

	private Connection connection;

	public Lost() {
		this.connection = database.getConnection();
	}


	private Books book;
	private Date loatDate;
	private String description;
	private int lostCount;


	private Lost select() {
		return null;
	}

	private Lost add() {
		return null;
	}

	private Lost update() {
		return null;
	}

	private boolean delet() {
		return false;
	}

}
