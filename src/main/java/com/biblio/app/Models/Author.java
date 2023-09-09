package com.biblio.app.Models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.SQLException;
import java.util.List;

import com.biblio.libs.Model;

@Data
@EqualsAndHashCode(callSuper = false)
public class Author{

	protected int id;
	protected String first_name;
	protected String last_name;

	protected List<Book> book = null;


	public void hasBooks(List<Book> book) {
		this.book = book;
	}

	public void setAuthor(int id,String authorFirstName, String authorLastName) {
		this.id = id;
		this.first_name = authorFirstName;
		this.last_name = authorLastName;
	}


}