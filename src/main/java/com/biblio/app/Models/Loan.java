package com.biblio.app.Models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = true)
public class Loan extends Book {



	private int id = 0;
	private String book_reference;
	private java.sql.Timestamp loan_date = null;
	private java.sql.Timestamp return_date = null;
	private java.sql.Timestamp expected_return_date = null;

	private User user;


	public void setLoan(int id,String bookReference, java.sql.Timestamp loanDate, java.sql.Timestamp returnDate, java.sql.Timestamp expectedReturnDate, User user){
		this.id = id;
		this.book_reference = bookReference;
		this.loan_date = loanDate;
		this.return_date = returnDate;
		this.expected_return_date = expectedReturnDate;
		this.user = user;
	}

	public void setLoan(User user,String bookReference,java.sql.Timestamp loanDate, java.sql.Timestamp expectedReturnDate, java.sql.Timestamp returnDate){
		this.user = user;
		this.book_reference = bookReference;
		this.loan_date = loanDate;
		this.expected_return_date = expectedReturnDate;
		this.return_date = returnDate;
	}

	public void setLoan(User user,String bookReference,java.sql.Timestamp loanDate, java.sql.Timestamp expectedReturnDate){
		this.user = user;
		this.book_reference = bookReference;
		this.loan_date = loanDate;
		this.expected_return_date = expectedReturnDate;
	}

	public Map<String, String> getLoan(){
		Map<String, String> LoanData = new HashMap<>();

//		new java.sql.Timestamp(System.currentTimeMillis());


//		LoanData.put("id", String.valueOf(this.getId()));
		LoanData.put("book", this.getIsbn());
		LoanData.put("user", this.user.getCnie());
		LoanData.put("book_reference", String.valueOf(this.book_reference));

		if(this.loan_date != null) {
			LoanData.put("loan_date", String.valueOf(this.loan_date));
		}
		if(this.expected_return_date != null) {
			LoanData.put("expected_return_date", String.valueOf(this.expected_return_date));
		}
		if(this.return_date != null){
			LoanData.put("return_date", String.valueOf(this.return_date));
		}
		return LoanData;
	}

	public void hasUser(User user){
		this.user = user;
	}

}