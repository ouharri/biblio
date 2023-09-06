package com.biblio.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.biblio.libs.Model;


@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Lost extends Model {

	protected int lostCount;
	protected java.sql.Date loastDate;
	protected String description;

	protected Book book;

	public Lost() {
		super("losts", new String[]{"id"});
	}

}