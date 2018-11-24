package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class DB {

	private static String user = "root";
	private static String password = "root";
	private static String connString = "jdbc:mysql://localhost:3306/snapsurvey?useSSL=false";
	
	private static Connection conn;
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static Connection getConnection() throws SQLException {
		Connection c = DriverManager.getConnection(connString, user, password);
		
		return c;
	}
	
	public static ResultSet execQuery(String query) throws SQLException {
		ResultSet rs;
		
		Connection c = conn == null ? getConnection() : conn;
		Statement s = c.createStatement();
		
		rs = s.executeQuery(query);
		
		return rs;
	}
	
	public static void execNonQuery(String query) throws SQLException {
		Connection c = conn == null ? getConnection() : conn;
		Statement s = c.createStatement();
		
		s.execute(query);
		
		if (conn == null)
			c.close();
	}
	
	public static void beginPersistantConnection() throws SQLException {
		conn = getConnection();
	}
	
	public static void terminatePersistantConnection() throws SQLException {
		conn.close();
		conn = null;
	}
	
}
