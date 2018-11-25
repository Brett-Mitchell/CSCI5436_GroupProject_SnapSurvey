package main.database;

import java.sql.SQLException;

public class SurveyResponseTextValue extends SurveyResponseValue {
	
	public String value;
	
	public static SelectBuilder<SurveyResponseTextValue> SELECT() {
		return new SelectBuilder<SurveyResponseTextValue>("survey_response_text_values", SurveyResponseTextValue.class)
					.joinOn("survey_response_values", new String [] {"id"});
	}
	
	public String getValue() { return this.value; }

	@Override
	public void set(String field, Object value) {
		switch (field) {
		case "value":
			this.value = (String)value;
			break;
		default:
			super.set(field, value);
			break;
		}
	}

	@Override
	public void create() {
		super.create();
		
		String q = "INSERT INTO survey_response_text_values (id, `value`) "
				 + "VALUES (" + this._id + ", '" + this.value + "');";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void update() {
		super.update();
		
		String q = "UPDATE survey_response_text_values "
				 + "SET `value`='" + this.value + "' "
				 + "WHERE id=" + this._id + "'";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	// delete is not overridden because of ON DELETE CASCADE restriction in db

}