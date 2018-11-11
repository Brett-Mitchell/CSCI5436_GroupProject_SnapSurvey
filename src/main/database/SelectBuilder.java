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
	private int limit = -1;
	private int offset = 0;
	private Map<String, String> orderBy = new HashMap<String, String>();
	private Class<T> clazz;
	
	public static enum ORDER_BY_DIRECTION {
		ASC,
		DESC
	}
	
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
	
	public SelectBuilder<T> where(String fieldName, String value) {
		return this.where(this.originalTable, fieldName, value);
	}
	
	public SelectBuilder<T> whereIn(String fieldName, String ...values) {
		return this.whereIn(this.originalTable, fieldName, values);
	}
	
	public SelectBuilder<T> limit(int limit) {
		this.limit = limit;
		return this;
	}
	
	public SelectBuilder<T> offset(int offset) {
		this.offset = offset;
		return this;
	}
	
	public SelectBuilder<T> orderBy(String fieldName, ORDER_BY_DIRECTION direction) {
		String ascDesc = "ASC";
		if (direction == ORDER_BY_DIRECTION.DESC)
			ascDesc = "DESC";
		this.orderBy.put(fieldName, ascDesc);
		return this;
	}
	
	public SelectBuilder<T> orderBy(String fieldName) {
		return this.orderBy(fieldName, ORDER_BY_DIRECTION.ASC);
	}
	
	// Get builds a SELECT statement from the current parameters and tables state and builds an array
	// of objects of type T from the ResultSet returned by MySQL
	public List<T> get() {
		String where = "";
		if (this.parameters.size() > 0) {
			where = " WHERE ";
			String and = "";
			// Serialize all parameters as a sequence of WHERE clauses
			for (Map.Entry<String, List<String>> p : this.parameters.entrySet()) {
				// If we are checking for more than one value, use WHERE ... IN [...]
				// Otherwise, use WHERE ...=...
				boolean in = p.getValue().size() > 1;
				String check = in ? " IN (" : "=";
				
				// Serialize all values for this particular parameter into a comma
				// separated list
				String comma = "";
				for (String s : p.getValue()) {
					check += comma + "'" + s + "'";
					comma = ",";
				}
				// Build the final where clause
				where += and + p.getKey() + check + (in ? ")" : "");
				and = " AND ";
			}
		}
		
		String limit = "";
		if (this.limit != -1)
			limit = " LIMIT " + this.offset + ", " + this.limit;
		
		String orderBy = "";
		if (!this.orderBy.isEmpty()) {
			orderBy = " ORDER BY ";
			for (Map.Entry<String, String> fieldEntry : this.orderBy.entrySet())
				orderBy += fieldEntry.getKey() + " " + fieldEntry.getValue() + ",";
			orderBy = orderBy.substring(0, orderBy.length() - 1);
		}
		
		// Build the final SELECT statement
		String q = "SELECT * FROM " + this.tables + where + limit + orderBy + ";";
		
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
					System.out.println(e.getMessage());
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return instances;
	}

}
