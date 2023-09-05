package com.biblio.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.SQLException;

import com.biblio.libs.db;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Author extends db {

	public int id;
	private Book[] book = null;
	public String firstName;
	public String lastName;

	public Author() {
		super("authors", new String[]{"id"});
	}


	public abstract boolean create() throws SQLException;

	public abstract Book read();

	public abstract boolean update() throws SQLException;

	public abstract boolean delete();
}