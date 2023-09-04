package com.biblio.model;

import com.biblio.dao.LoanDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class Loans extends LoanDao {

	public int book;
	public int user;
	public int bookReference;
	public String isbn;
	public java.sql.Timestamp loanDate;
	public java.sql.Timestamp expectedReturnDate;
	public java.sql.Timestamp returnDate;

}
