package com.biblio.model;

import com.biblio.libs.db;
import lombok.Data;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public @Data class Books extends db{

	private int isbn;
	private int quantities;
	private int pages;
	private String title;
	private String edition;
	private String language;
	//private String category;
	private String description;

	public Books() {
		super("books");
	}

	public Books setBook(int isbn, int quantities, int pages, String title, String edition, String language, String description) {
		this.isbn = isbn;
		this.quantities = quantities;
		this.pages = pages;
		this.title = title;
		this.edition = edition;
		this.language = language;
		this.description = description;
		return this;
	}

	public boolean save() throws SQLException {
		Map<String, String> bookData = new HashMap<>();
		bookData.put("isbn", String.valueOf(this.isbn));
		bookData.put("quantities", String.valueOf(this.quantities));
		bookData.put("pages", String.valueOf(this.pages));
		bookData.put("title", this.title);
		bookData.put("edition", this.edition);
		bookData.put("language", this.language);
		bookData.put("description", this.description);

		return this.create(bookData);
	}


}
