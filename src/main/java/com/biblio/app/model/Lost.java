package com.biblio.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.biblio.libs.db;


@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Lost extends db {

	public int lostCount;
	public Book book;
	public java.sql.Date loastDate;
	public String description;

	public Lost() {
		super("losts", new String[]{"id"});
	}

}