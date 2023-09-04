package com.biblio.model;

import com.biblio.dao.BookDao;
import com.biblio.libs.db;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class Book extends BookDao {

	public int quantities;
	public int pages;
	public String isbn;
	public String title;
	public String edition;
	public String language;
	public String description;
	public java.sql.Timestamp delete_at;

	public Category[] category;
	public Author[] author;

	public void setBook(String isbn, int quantities, int pages, String title, String edition, String language, String description) {
		this.isbn = isbn;
		this.quantities = quantities;
		this.pages = pages;
		this.title = title;
		this.edition = edition;
		this.language = language;
		this.description = description;
	}


	public boolean create() throws SQLException {
		Map<String, String> bookData = new HashMap<>();

		bookData.put("isbn", this.isbn);
		bookData.put("quantities", String.valueOf(this.quantities));
		bookData.put("pages", String.valueOf(this.pages));
		bookData.put("title", this.title);
		bookData.put("edition", this.edition);
		bookData.put("language", this.language);
		bookData.put("description", this.description);

		this.beginTransaction();

		if (!this.create(bookData)) {
			this.rollbackTransaction();
			return false;
		}

		this.commitTransaction();


		return true;
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