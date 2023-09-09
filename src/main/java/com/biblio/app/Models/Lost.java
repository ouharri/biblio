package com.biblio.app.Models;

import com.biblio.app.Enums.LostStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.SQLException;


@Data
@EqualsAndHashCode(callSuper = false)
public class Lost extends Book{

	private int id;
	private String book_reference;
	private java.sql.Date loast_date;
	private String description;
	private LostStatus status;

	public void setLost(int id,String book_reference, java.sql.Date loastDate, String description, LostStatus status){
		this.id = id;
		this.book_reference = book_reference;
		this.loast_date = loastDate;
		this.description = description;
		this.status = status;
	}


}