package com.biblio.model;

import java.util.Date;

public class Borrown {

	private int id;
	private Book book;
	private User user;
	private Date loanDate;
	private Date returnDate;
	private Date expectedReturnDate;
	private int bookReference;

}
