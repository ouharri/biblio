package com.biblio.model;

import com.biblio.core.database;

public class books extends database {

	private int isbn;
	private String title;
	private String pages;
	private String edition;
	private String language;
	private String category;
	private String description;
	private String quantities;

	public boolean add() {
		return false;
	}

	public boolean update() {
		return false;
	}

	public boolean delete() {
		return false;
	}

	public books select() {
		return null;
	}

	public books search() {
		return null;
	}

}
