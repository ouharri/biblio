package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

public class Member extends User {

	private Connection connection;

	public Member() {
		this.connection = database.getConnection();
	}

	public void borrow() {

	}

}
