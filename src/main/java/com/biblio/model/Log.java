package com.biblio.model;

import java.sql.Connection;
import com.biblio.core.database;

import com.biblio.dao.LogDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Log extends LogDao {

	public int id;
	public int user;
	public int book;
	public java.sql.Timestamp create_at;

}
