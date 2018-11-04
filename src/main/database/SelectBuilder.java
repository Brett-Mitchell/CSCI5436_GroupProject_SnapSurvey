package main.database;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectBuilder<T extends Table> {
	
	private Map<String, List<String>> parameters = new HashMap<String, List<String>>();
	private String originalTable;
	private String tables;
	private Class<T> clazz;
	
	public SelectBuilder(String table, Class<T> clazz) {
		this.originalTable = table;
		this.tables = table;
		this.clazz = clazz;
	}
	
	public SelectBuilder<T> joinOn(String table, String ...joinFields) {
		String joinFieldsStr = "";
		String and = "";
		for (String jf : joinFields) {
			joinFieldsStr += and + this.originalTable + "." + jf + "=" + table + "." + jf;
			and = " AND ";
		}
		this.tables += " JOIN " + table + " ON " + joinFieldsStr;
		return this;
	}
	
	public SelectBuilder<T> where(String table, String fieldName, String value) {
		this.parameters.put(table + "." + fieldName, new ArrayList<String>() {{add(value);}});
		return this;
	}
	
	public SelectBuilder<T> whereIn(String table, String fieldName, String ...values) {
		this.parameters.put(table + "." + fieldName, new ArrayList<String>() {{ for (String v : values) add(v); }});
		return this;
	}
	
	// Get builds a SELECT statement from the current parameters and tables state and builds an array
	// of objects of type T from the ResultSet returned by MySQL
	public List<T> get() {
		String where = " WHERE ";
		String and = "";
		// Serialize all parameters as a sequence of WHERE clauses
		for (Map.Entry<String, List<String>> p : this.parameters.entrySet()) {
			// If we are checking for more than one value, use WHERE ... IN [...]
			// Otherwise, use WHERE ...=...
			boolean in = p.getValue().size() > 1;
			String check = in ? " IN [" : "=";
			
			// Serialize all values for this particular parameter into a comma
			// separated list
			String comma = "";
			for (String s : p.getValue()) {
				check += comma + "'" + s + "'";
				comma = ",";
			}
			// Build the final where clause
			where += and + p.getKey() + check + (in ? "]" : "");
			and = " AND ";
		}
		// Build the final SELECT statement
		String q = "SELECT * FROM " + this.tables + where + ";";
		System.out.println(q);

		List<T> instances = new ArrayList<T>();
		
		try {
			// Execute build select query
			ResultSet rs = DB.execQuery(q);
			// Get a list of the column names returned by the query
			List<String> columnNames = new ArrayList<String>();
			ResultSetMetaData meta = rs.getMetaData();
			// Column names are 1 indexed instead of 0 indexed
			for (int i = 1; i <= meta.getColumnCount(); i++)
				columnNames.add(meta.getColumnName(i));
			while(rs.next()) {
				try {
					// Create instances of the given generic type and set their fields with 
					T inst = clazz.getDeclaredConstructor().newInstance();
					for (String c : columnNames)
						inst.set(c, rs.getObject(c));
					instances.add(inst);
				} catch (InstantiationException |
						 IllegalAccessException |
						 NoSuchMethodException  |
						 InvocationTargetException e) {
					
				}
			}
		} catch (SQLException e) {}
		
		return instances;
	}

}
