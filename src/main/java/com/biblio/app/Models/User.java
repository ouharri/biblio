package com.biblio.app.Models;

import com.biblio.app.Enums.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class User{

	protected String cnie;
	protected String first_name;
	protected String last_name;
	protected String email;
	protected String phone;
	protected Gender gender;
	protected String password;
	protected java.sql.Timestamp delete_at = null;

	protected List<Role> roles = new ArrayList<Role>();
	protected List<Loan> Loan = new ArrayList<Loan>();
	protected List<Log> Log = new ArrayList<Log>();

	public Map<String, String> getUser(){
		Map<String, String> UserData = new HashMap<>();
		UserData.put("cnie", this.cnie);
		UserData.put("first_name", this.first_name);
		UserData.put("last_name", this.last_name);
		UserData.put("email", this.email);
		UserData.put("phone", String.valueOf(this.phone));
		UserData.put("gender", String.valueOf(this.gender));
		UserData.put("password", this.password);
		return UserData;
	}

	public void setUser(String cnie,String firstName, String lastName, Gender gender, String email, String phone, String password ){
		this.cnie = cnie;
		this.first_name = firstName;
		this.last_name = lastName;
		this.gender = Gender.valueOf(String.valueOf(gender));
		this.email = email;
		this.phone = phone;
		this.password = password;
	}


	public void hasRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void hasLoans(List<Loan> loans) {
		this.Loan = loans;
	}

	public void hasLogs(List<Log> logs) {
		this.Log = logs;
	}

    public String getFullName() {
		return this.first_name + " " + this.last_name;
    }
}