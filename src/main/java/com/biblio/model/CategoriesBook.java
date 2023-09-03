package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class CategoriesBook {

	private Connection connection;

	public CategoriesBook() {
		this.connection = database.getConnection();
	}

	private Books book;
	private Categories category;

}
