package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class User implements Table {
	
	protected int id = -1;
	protected String username;
	protected String password;
	protected String email;
	
	protected abstract int getUserType();
	
	public boolean authenticate() {
		String q = "CALL authenticate_user('" + this.username + "','" + this.password + "');";
		try {
			ResultSet rs = DB.execQuery(q);
			rs.next();
			return rs.getInt("valid") == 1;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public User() {}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User(int id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	public int getId() { return this.id; }
	public String getUsername() { return this.username; }
	public String getPassword() { return this.password; }
	public String getEmail() { return this.email; }
	
	public void set(String field, Object value) {
		switch (field) {
		case "id":
			this.id = ((Integer)value).intValue();
			break;
		case "username":
			this.username = (String)value;
			break;
		case "password":
			this.password = (String)value;
			break;
		case "email":
			this.email = (String)value;
			break;
		}
	}
	
	public void create() {
		String q = "CALL new_user('" + username
				 + "','" + password
				 + "','" + email
				 + "',"   + getUserType() + ");";
		
		try {
			ResultSet rs = DB.execQuery(q);
			rs.next();
			this.id = rs.getInt("id");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void update() {
		String q = "CALL update_user(" + id + ",'" + username + "','" + password + "', '" + email + "');";
		
		try {
			DB.execQuery(q);
		} catch (SQLException e) {
			
		}
	}
	
	public void delete() {
		String q;
		if (id != -1) {
			q = "DELETE FROM users WHERE id=" + id + ";";
		} else {
			q = "DELETE FROM users WHERE username='" + username + "';";
		}
		
		try {
			DB.execNonQuery(q);
		} catch (SQLException e) {
			
		}
	}
	
}
