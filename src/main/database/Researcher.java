package main.database;

import java.sql.ResultSet;

public class Researcher extends User {
	
	protected String subTable = "researchers";
	
	public Researcher(String username, String password) {
		super(username, password);
	}
	
	public Researcher(int id, String username, String password) {
		super(id, username, password);
	}

	protected int getUserType() {
		return 0;
	}
	
	public static ResultSet get(int ...ids) {
		return User.get("researchers", ids);
	}
	
	public static ResultSet get(String ...usernames) {
		return User.get("researchers", usernames);
	}
	
}
