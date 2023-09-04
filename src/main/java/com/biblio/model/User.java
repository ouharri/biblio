package com.biblio.model;

import com.biblio.dao.UserDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends UserDao {

	public int id;
	public String firstName;
	public String lastName;
	public String email;
	public int phone;
	public String gender;
	public String password;
	public java.sql.Timestamp delete_at;

}
