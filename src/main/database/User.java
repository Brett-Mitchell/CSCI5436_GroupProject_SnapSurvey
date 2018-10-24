package main.database;

public class User extends Model {

	static {
		User.TABLE_NAME = "users";
		
		FIELDS.put("username", new Field(Field.SQL_TYPE.VARCHAR));
		FIELDS.put("salt", new Field(Field.SQL_TYPE.VARCHAR));
		FIELDS.put("auth_hash", new Field(Field.SQL_TYPE.VARCHAR));
	}
	
	User() {
		
	}
	
}
