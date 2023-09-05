package com.biblio.app.model;

import com.biblio.dao.RoleDao;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.biblio.libs.db;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class User extends db {

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

	public String hashPassword(String plainTextPassword) {
		String salt = BCrypt.gensalt();
		return BCrypt.hashpw(plainTextPassword, salt);
	}

	public boolean checkPassword(String plainTextPassword, String hashedPassword) {
		return BCrypt.checkpw(plainTextPassword, hashedPassword);
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