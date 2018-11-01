package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class User implements Table {
	
	public int id = -1;
	public String username;
	public String password;
	protected String salt;
	protected String auth_hash;
	
	protected abstract int getUserType();
	
	public boolean validAuth() {
		String q = "CALL authenticate_user('" + this.username + "','" + this.password + "');";
		try {
			ResultSet rs = DB.execQuery(q);
			rs.next();
			return rs.getInt("valid") == 1;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User(int id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	public static ResultSet get(String userSubType, String ...usernames) {
		String str_usernames = "(";
		for (String username : usernames)
			str_usernames += "'" + username + "',";
		
		str_usernames = str_usernames.substring(0, str_usernames.length() - 1) + ")";

		String joinStr = userSubType == "NONE" ? "" : "JOIN " + userSubType + " ON users.id=" + userSubType + ".id ";
		String q = "SELECT * FROM users " + joinStr + "WHERE username in " + str_usernames + ";";
		
		System.out.println(q);
		
		try {
			return DB.execQuery(q);
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static ResultSet get(String userSubType, int ...ids) {
		String str_ids = "(";
		for (int id : ids)
			str_ids += Integer.toString(id) + ",";
		
		str_ids = str_ids.substring(0, str_ids.length() - 1) + ")";
		
		String joinStr = userSubType == "NONE" ? "" : "JOIN " + userSubType + " ON users.id=" + userSubType + ".id ";
		String q = "SELECT * FROM users " + joinStr + "WHERE id in " + str_ids + ";";
		
		System.out.println(q);
		
		try {
			return DB.execQuery(q);
		} catch (SQLException e) {
			return null;
		}
	}
	
	public void create() {
		String q = "CALL new_user('" + username
				 + "','" + password
				 + "',"   + getUserType() + ");";
		
		try {
			ResultSet rs = DB.execQuery(q);
			rs.next();
			this.id = rs.getInt("id");
		} catch (SQLException e) {
			
		}
	}
	
	public void update() {
		String q = "CALL update_user(" + id + ",'" + username + "','" + password + "');";
		
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
