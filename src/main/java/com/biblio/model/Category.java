package com.biblio.model;

import com.biblio.dao.CategoryDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends CategoryDao {

	private int id;
	private Book[] book = null;
	private String category;
	private String description;
	private java.sql.Timestamp delete_at;

}