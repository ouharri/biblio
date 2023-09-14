package com.biblio.app.Models;

import com.biblio.app.Enums.LostStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = false)
public class Lost extends Book{

	private String book_reference;
	private java.sql.Date lost_date = null;
	private String description = null;
	private LostStatus actual_status = null;

	public void setLost(String book_reference, java.sql.Date loastDate, String description, LostStatus status){
		this.book_reference = book_reference;
		this.lost_date = loastDate;
		this.description = description;
		this.actual_status = status;
	}

	public void setLost(String book_reference, String description, LostStatus status){
		this.book_reference = book_reference;
		this.description = description;
		this.actual_status = status;
	}

	public void setLost(String book_reference, String description){
		this.book_reference = book_reference;
		this.description = description;
	}

	public Map<String, String> getLost(){
		Map<String, String> LoanData = new HashMap<>();


		LoanData.put("book", this.getIsbn());
		LoanData.put("book_reference", String.valueOf(this.book_reference));

		if(this.lost_date != null) {
			LoanData.put("lost_date", String.valueOf(this.lost_date));
		}
		if(this.description != null) {
			LoanData.put("description", String.valueOf(this.description));
		}
		if(this.actual_status != null){
			LoanData.put("actual_status", String.valueOf(this.actual_status));
		}
		return LoanData;
	}


}