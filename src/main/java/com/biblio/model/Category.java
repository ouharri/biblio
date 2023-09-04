package com.biblio.model;

import com.biblio.dao.CategoryDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends CategoryDao {

	public int id;
	public String category;
	public String description;
	public java.sql.Timestamp delete_at;

}
