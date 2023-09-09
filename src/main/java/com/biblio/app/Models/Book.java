package com.biblio.app.Models;

import com.biblio.app.Enums.Language;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class Book {

	protected int quantities;
	protected int pages;
	protected String isbn;
	protected String title;
	protected String edition;
	protected Language language;
	protected String description;
	protected java.sql.Timestamp delete_at;

	protected List<Category> categories = new ArrayList<Category>();
	protected List<Author> authors = new ArrayList<Author>();

	protected List<Loan> loans = new ArrayList<Loan>();
	protected List<Lost> Losts = new ArrayList<Lost>();

	protected List<WaitingList> waitingLists = new ArrayList<WaitingList>();
	protected List<Log> logs = new ArrayList<Log>();




	public void setBook(String isbn, int quantities, int pages, String title, String edition, Language language, String description) {
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
		bookData.put("language", this.language.toString());
		bookData.put("description", this.description);
		return bookData;
	}



	public void hasAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public void hasCategories(List<Category> categories) {
		this.categories = categories;
	}

	public void hasWaitingLists(List<WaitingList> waitingLists) {
		this.waitingLists = waitingLists;
	}

	public void hasLoans(List<Loan> loans) {
		this.loans = loans;
	}

	public void hasLosts(List<Lost> Losts) {
		this.Losts = Losts;
	}

	public void hasLogs(List<Log> logs) {
		this.logs = logs;
	}


}