package com.biblio.model;

import com.biblio.dao.AuthorDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Author extends AuthorDao {

	public int id;
	public String firstName;
	public String lastName;

}