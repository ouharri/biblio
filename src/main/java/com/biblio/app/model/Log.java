package com.biblio.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.biblio.libs.db;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Log extends db {

	public int id;
	public User user;
	public Book book;
	public java.sql.Timestamp create_at;

	public Log() {
		super("logs", new String[]{"id"});
	}

}