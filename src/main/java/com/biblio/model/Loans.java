package com.biblio.model;

import com.biblio.dao.LoanDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Loans extends LoanDao {

	public int bookReference;
	public Book book;
	public User user;
	public String isbn;
	public java.sql.Timestamp loanDate;
	public java.sql.Timestamp expectedReturnDate;
	public java.sql.Timestamp returnDate;

}