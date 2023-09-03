package com.biblio.model;

import java.util.Date;
import java.sql.Connection;
import com.biblio.core.database;


public class lost {

	private Connection connection;

	public lost() {
		this.connection = database.getConnection();
	}


	private books book;
	private Date loatDate;
	private String description;
	private int lostCount;


	private lost select() {
		return null;
	}

	private lost add() {
		return null;
	}

	private lost update() {
		return null;
	}

	private boolean delet() {
		return false;
	}

}
