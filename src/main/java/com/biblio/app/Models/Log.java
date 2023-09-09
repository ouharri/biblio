package com.biblio.app.Models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.biblio.libs.Model;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Log {

	protected int id;
	protected java.sql.Timestamp create_at;

	protected User user;
	protected List<Book> books = new ArrayList<Book>();

}