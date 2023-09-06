package com.biblio.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.biblio.libs.Model;

import java.util.HashMap;
import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Loan extends Model {

	public Loan() {
		super("loans", new String[]{"id"});
		this._softDelete = false;
	}

	protected int bookReference ;
	protected Book book;
	protected User user;

	public java.sql.Timestamp loanDate;
	public java.sql.Timestamp expectedReturnDate = null;
	public java.sql.Timestamp returnDate = null;

	public Map<String, String> getLoan(){
		Map<String, String> LoanData = new HashMap<>();

		this.bookReference = (this.getColumnCount("book", book.getIsbn()) + 1);
		this.loanDate = new java.sql.Timestamp(System.currentTimeMillis());

		LoanData.put("book", this.book.getIsbn());
		LoanData.put("user", String.valueOf(this.user.getId()));
		LoanData.put("loanDate", String.valueOf(this.loanDate));
//		LoanData.put("expectedReturnDate", String.valueOf(this.expectedReturnDate));
//		LoanData.put("returnDate", String.valueOf(this.returnDate));
		LoanData.put("bookReference", String.valueOf(this.bookReference));
		return LoanData;
	}

	public void setLoan(Book book, User user){
		this.book = book;
		this.user = user;
	}

}