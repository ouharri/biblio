package com.biblio.model;

import com.biblio.dao.LogDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Log extends LogDao {

	public int id;
	public User user;
	public Book book;
	public java.sql.Timestamp create_at;

}