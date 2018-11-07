package main.database;

import java.sql.SQLException;

public class SurveyResponseTextValue extends SurveyResponseValue {
	
	public String text;
	
	public static SelectBuilder<SurveyResponseTextValue> SELECT() {
		return new SelectBuilder<SurveyResponseTextValue>("survey_response_text_values", SurveyResponseTextValue.class)
					.joinOn("survey_response_values", "id");
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
		case "text":
			this.text = (String)value;
			break;
		default:
			super.set(field, value);
			break;
		}
	}

	@Override
	public void create() {
		super.create();
		
		String q = "INSERT INTO survey_response_text_values (id, text) "
				 + "VALUES (" + this._id + ", '" + this.text + "');";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}

	@Override
	public void update() {
		super.update();
		
		String q = "UPDATE survey_response_text_values "
				 + "SET text='" + this.text + "' "
				 + "WHERE id=" + this._id + "'";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}
	
	// delete is not overridden because of ON DELETE CASCADE restriction in db

}