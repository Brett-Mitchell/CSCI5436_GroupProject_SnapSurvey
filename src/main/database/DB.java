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
	private static String connString = "jdbc:mysql://localhost:3306/imdb?useSSL=false";
	
	private static List<String> tableNames = Arrays.asList(
			"actors",
			"actors2",
			"directors",
			"directors_genres",
			"movies",
			"movies_directors",
			"movies_genres",
			"roles"
	);

	private static Connection getConnection() throws SQLException {
		Connection c = DriverManager.getConnection(connString, user, password);
		
		return c;
	}
	
	public static ResultSet get(String table) throws SQLException {
		ResultSet rs;
		
		Connection c = getConnection();
		Statement s = c.createStatement();
		
		String q =
				  "SELECT * "
				+ "FROM " + table + ";";
		
		rs = s.executeQuery(q);
		
		return rs;
	}
}
