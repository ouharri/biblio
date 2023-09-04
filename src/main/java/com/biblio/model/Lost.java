package com.biblio.model;

import java.util.Date;
import java.sql.Connection;
import com.biblio.core.database;

import com.biblio.dao.LostDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Lost extends LostDao {

	public int book;
	public java.sql.Date loastDate;
	public int lostCount;
	public String description;

}
