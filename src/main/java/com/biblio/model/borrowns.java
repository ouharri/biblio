package com.biblio.model;

import java.util.Date;
import java.sql.Connection;
import com.biblio.core.database;

public class borrowns {

	private int id;
	private books book;
	private users user;
	private Date loanDate;
	private Date returnDate;
	private Date expectedReturnDate;
	private int bookReference;

}
