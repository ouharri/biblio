package com.biblio.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.biblio.libs.db;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Book extends db {

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

	public Book() {
		super("books",new String[]{"isbn"});
	}

	public void setBook(String isbn, int quantities, int pages, String title, String edition, String language, String description) {
		this.isbn = isbn;
		this.quantities = quantities;
		this.pages = pages;
		this.title = title;
		this.edition = edition;
		this.language = language;
		this.description = description;
	}

	public Map<String, String> getBook() {
		Map<String, String> bookData = new HashMap<>();
		bookData.put("isbn", this.isbn);
		bookData.put("quantities", String.valueOf(this.quantities));
		bookData.put("pages", String.valueOf(this.pages));
		bookData.put("title", this.title);
		bookData.put("edition", this.edition);
		bookData.put("language", this.language);
		bookData.put("description", this.description);
		return bookData;
	}

	public void hasAuthors(Author[] authors) {
		this.author = authors;
	}

	public void hasCategories(Category[] categories) {
		this.category = categories;
	}

	public abstract boolean create() throws SQLException;

	public abstract Book read();

	public abstract boolean update() throws SQLException;

	public abstract boolean delete();
}