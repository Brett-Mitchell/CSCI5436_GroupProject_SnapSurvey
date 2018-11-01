package main.database;

import java.sql.ResultSet;

public class Participant extends User {

	public Participant(String username, String password) {
		super(username, password);
	}
	
	public Participant(int id, String username, String password) {
		super(id, username, password);
	}

	@Override
	protected int getUserType() {
		return 1;
	}
	
	public static ResultSet get(int ...ids) {
		return User.get("participants", ids);
	}
	
	public static ResultSet get(String ...usernames) {
		return User.get("participants", usernames);
	}
	
}
