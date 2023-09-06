package com.biblio.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.SQLException;
import java.util.List;

import com.biblio.libs.Model;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Author extends Model {

	protected int id;
	protected String firstName;
	protected String lastName;

	protected List<Book> book = null;

	public Author() {
		super("authors", new String[]{"id"});
	}

	public void hasBooks(List<Book> book) {
		this.book = book;
	}

	public abstract boolean create() throws SQLException;

	public abstract Book read();

	public abstract boolean update() throws SQLException;

	public abstract boolean delete();

	public void setAuthor(int id,String authorFirstName, String authorLastName) {
		this.id = id;
		this.firstName = authorFirstName;
		this.lastName = authorLastName;
	}
}