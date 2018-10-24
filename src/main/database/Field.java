package main.database;

public class Field {
	
	private SQL_TYPE sqlType;
	
	public static enum SQL_TYPE {
		INT,
		SMALLINT,
		TINYINT,
		BYTE,
		BIT,
		VARCHAR
	}
	
	Field(SQL_TYPE sqlType) {
		this.sqlType = sqlType;
	}
	
	public SQL_TYPE getSqlType() { return this.sqlType; }
	
}
