package com.biblio.model;

import com.biblio.dao.LostDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Lost extends LostDao {

	public int lostCount;
	public Book book;
	public java.sql.Date loastDate;
	public String description;

}