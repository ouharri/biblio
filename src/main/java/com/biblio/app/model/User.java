package com.biblio.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.biblio.libs.Model;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class User extends Model {

	public User() {
		super("users", new String[]{"id"});
	}

	protected int id;
	protected String firstName;
	protected String lastName;
	protected String email;
	protected String phone;
	protected String gender;
	protected String password;
	protected java.sql.Timestamp delete_at = null;

	protected List<Role> roles = new ArrayList<Role>();
	protected List<Loan> Loan = new ArrayList<Loan>();
	protected List<Log> Log = new ArrayList<Log>();

	public Map<String, String> getUser(){
		Map<String, String> UserData = new HashMap<>();
		UserData.put("firstName", this.firstName);
		UserData.put("lastName", this.lastName);
		UserData.put("email", this.email);
		UserData.put("phone", String.valueOf(this.phone));
		UserData.put("gender", this.gender);
		UserData.put("password", this.password);
		return UserData;
	}

	public void setUser(String firstName, String lastName, String email, String password , String gender, String phone){
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.phone = phone;
		this.password = password;
	}

	public void setUser(String id,String firstName, String lastName, String email, String password , String gender, String phone){
		this.id = Integer.parseInt(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.phone = phone;
		this.password = password;
	}


	public void hasRoles(List<Role> roles) {
		this.roles = roles;
	}


	public abstract boolean create() throws SQLException;

	public abstract User read();

	public abstract User getUserWithRoles();

	public abstract User getByEmailWithRoles();

	public abstract boolean update() throws SQLException;

	public abstract boolean delete();
}