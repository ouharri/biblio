package com.biblio.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.biblio.libs.Model;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class User extends Model {

	public User() {
		super("users", new String[]{"id"});
	}

	public int id;
	public String firstName;
	public String lastName;
	public String email;
	public String phone;
	public String gender;
	public String password;
	public java.sql.Timestamp delete_at = null;

	protected Role[] roles = null;

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


	public void hasRoles(Role[] roles) {
		this.roles = roles;
	}




	public abstract boolean create() throws SQLException;

	public abstract User read();

	public abstract User getUserWithRoles();

	public abstract User getByEmailWithRoles();

	public abstract boolean update() throws SQLException;

	public abstract boolean delete();
}