package com.biblio.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.biblio.libs.Model;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Log extends Model {

	protected int id;
	protected java.sql.Timestamp create_at;

	protected User user;
	protected Book book;

	public Log() {
		super("logs", new String[]{"id"});
	}

}