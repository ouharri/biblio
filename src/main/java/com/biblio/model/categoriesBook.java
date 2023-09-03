package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class categoriesBook {

	private Connection connection;

	public categoriesBook() {
		this.connection = database.getConnection();
	}

	private books book;
	private categories category;

}
