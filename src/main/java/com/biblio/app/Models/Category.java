package com.biblio.app.Models;

import lombok.Data;
import lombok.EqualsAndHashCode;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class Category{


	protected int id;
	protected String category;
	protected String description;
	protected java.sql.Timestamp delete_at;

	protected List<Book> book = null;

	public Map<String, String> getCategory() {
		Map<String, String> CategoryData = new HashMap<>();
		CategoryData.put("id", String.valueOf(this.id));
		CategoryData.put("category", this.category);
		CategoryData.put("description", this.description);
		return CategoryData;
	}

	public void setCategory(String category, String description) {
		this.category = category;
		this.description = description;
	}

	public void setCategory(int id ,String category, String description) {
		this.id = id;
		this.category = category;
		this.description = description;
	}

	public void hasBooks(List<Book> book) {
		this.book = book;
	}


}