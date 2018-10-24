package main.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// The Model class acts as the basis for all database access models
// The Model class assumes an integer id field in all models represented.
public class Model<T extends Model> {
	
	protected static String TABLE_NAME;
	protected static Map<String, Field> FIELDS = new HashMap<String, Field>();
	
	static {
		FIELDS.put("id", new Field(Field.SQL_TYPE.INT));
	}
	
	public static String tableName() { return TABLE_NAME; }
	
	public static Field field(String fieldName) {
		return FIELDS.get(fieldName);
	}
	
	public static T 
	
	private Map<String, FieldInstance> _fields = new HashMap<String, FieldInstance>();
	
	public static List<T> processRows(ResultSetContainer r) {
		List<T> models = new ArrayList<T>();
		
		for (HashMap<String, Object> row : r) {
			Model model = new Model();
			for (String column : row.keySet()) {
				model._fields.get(column).set(row.get(column));
			}
		}
		
		return models;
	}
	
	Model() { }
	
	private void push() {
		String serializedChanges = this.serializeChanges();
	}
	
	public Object fieldObjectValue(String fieldName) {
		return this._fields.get(fieldName).asObject();
	}
	
	public String serializeChanges() {
		String serialized = "";
		
		for (String field : FIELDS.keySet()) {
			FieldInstance f = this._fields.get(field);
			if (f.shouldUpdate()) {
				serialized += f.serializeChanges();
			}
		}
		
		return serialized;
	}
	
}
