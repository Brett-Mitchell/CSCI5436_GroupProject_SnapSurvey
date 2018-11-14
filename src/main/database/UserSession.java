package main.database;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class UserSession implements Table {
	
	public int user;
	public String id;
	public Timestamp expiry;
	
	public static SelectBuilder<UserSession> SELECT() {
		return new SelectBuilder<UserSession>("user_sessions", UserSession.class);
	}
	
	public static UserSession fromSessionCookie(HttpServletRequest request) {
		String session = "";
		
		if (request.getCookies() == null) return null;
		
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("session")) {
				session = c.getValue();
			}
		}
		
		if (session == "") return null;
		
		List<UserSession> matchingSessions = SELECT().where("id", session).get();
		
		if (matchingSessions.isEmpty()) return null;
		
		return matchingSessions.get(0);
	}
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

	public static String newSessionId() {
		byte[] bytes = new byte[64];
		(new SecureRandom()).nextBytes(bytes);
		return bytesToHex(bytes);
	}
	
	public boolean expired() {
		return this.expiry.compareTo(new Timestamp(Calendar.getInstance().getTime().getTime())) > 0;
	}
	
	public User getUser() {
		List<User> userList = (List<User>)((List<?>)Researcher.SELECT().where("id", Integer.toString(this.user)).get());
		if (userList.isEmpty())
			userList = (List<User>)((List<?>)Participant.SELECT().where("id", Integer.toString(this.user)).get());
		
		if (userList.isEmpty()) return null;
		
		return userList.get(0);
	}
	
	@Override
	public void set(String field, Object value) {
		switch (field) {
		case "user":
			this.user = ((Integer)value).intValue();
			break;
		case "id":
			this.id = (String)value;
			break;
		case "expiry":
			this.expiry = (Timestamp)value;
			break;
		}
	}

	@Override
	public void create() {
		String q = "INSERT INTO user_sessions (user, id, expiry) "
				 + "VALUES (" + this.user + ", '" + this.id + "', NOW() + INTERVAL 15 MINUTE);";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void update() {
		String q = "UPDATE user_sessions "
				 + "SET expiry=NOW() + INTERVAL 15 MINUTE "
				 + "WHERE user=" + this.user + " AND id='" + this.id + "';";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {
			
		}
	}

	@Override
	public void delete() {
		String q = "DELETE FROM user_sessions "
				 + "WHERE user=" + this.user + " AND id='" + this.id + "';";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
