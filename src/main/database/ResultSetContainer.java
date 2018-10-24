package main.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ResultSetContainer implements Iterable<HashMap<String, Object>> {
	
	private String tableName;
	private List<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();
	private List<String> columns = new ArrayList<String>();
	
	// ResultSetContainer constructor
	// @param rs: The result set to process. This result set is assumed to be from a single MySQL table
	ResultSetContainer(ResultSet rs) throws SQLException {
		ResultSetMetaData rsMeta = rs.getMetaData();
		
		int columnCount = rsMeta.getColumnCount();
		
		for (int i = 1; i <= columnCount; i++)
			columns.add(rsMeta.getColumnName(i));
		
		// Get the table name for the result set (assumed that only one table is contained in rs)
		this.tableName = rsMeta.getTableName(1);
		
		while(rs.next()) {
			// Create a new row
			HashMap<String, Object> row = new HashMap<String, Object>();
			
			// Add the column values as key-value pairs to the row
			for (String column : columns) {
				Object o = rs.getObject(column);
				if (o == null) o = "NULL";
				row.put(column, o);
			}
			
			// Add the row to the results list
			this.results.add(row);
		}
	}
	
	public String getTableName() { return this.tableName; }
	
	public int getRowCount() { return this.results.size(); }
	
	public int getColumnCount() { return this.columns.size(); }
	
	public String getColumnName(int column) { return this.columns.get(column); }
	
	public Object getValue(int row, String columnName) {
		return this.results.get(row).get(columnName);
	}

	@Override
	public Iterator<HashMap<String, Object>> iterator() {
		Iterator<HashMap<String, Object>> i = this.results.iterator();
		return new Iterator<HashMap<String, Object>>() {
			@Override
			public boolean hasNext() {
				return i.hasNext();
			}
			
			@Override
			public HashMap<String, Object> next() {
				return i.next();
			}
		};
	}
}
