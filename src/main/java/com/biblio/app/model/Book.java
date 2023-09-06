package com.biblio.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biblio.libs.Model;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Book extends Model {

	protected int quantities;
	protected int pages;
	protected String isbn;
	protected String title;
	protected String edition;
	protected String language;
	protected String description;
	protected java.sql.Timestamp delete_at;

	protected List<Category> categories = new ArrayList<Category>();
	protected List<Author> authors = new ArrayList<Author>();
	protected List<WaitingList> waitingLists = new ArrayList<WaitingList>();

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

	public void hasAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public void hasCategories(List<Category> categories) {
		this.categories = categories;
	}

	public abstract boolean create() throws SQLException;

	public abstract Book read();

	public abstract boolean update() throws SQLException;

	public abstract boolean delete();
}