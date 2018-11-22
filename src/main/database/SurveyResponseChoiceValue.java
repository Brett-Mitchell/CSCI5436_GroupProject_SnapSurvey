package main.database;

import java.sql.SQLException;

public class SurveyResponseChoiceValue extends SurveyResponseValue {
	
	private int _choice;
	
	public static SelectBuilder<SurveyResponseChoiceValue> SELECT() {
		return new SelectBuilder<SurveyResponseChoiceValue>("survey_response_choice_values", SurveyResponseChoiceValue.class)
				.joinOn("survey_response_values", new String[] {"id"});
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
		case "choice":
			this._choice = ((Integer)value).intValue();
			break;
		default:
			super.set(field, value);
			break;
		}
	}

	@Override
	public void create() {
		super.create();
		
		String q = "INSERT INTO survey_response_choice_values (id, choice) "
				 + "VALUES (" + this._id + ", " + this._choice + ");";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}

	@Override
	public void update() {
		super.update();
		
		String q = "UPDATE survey_response_choice_values "
				 + "SET choice=" + this._choice + " "
				 + "WHERE id=" + this._id + "'";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}
	
	// delete is not overridden because of ON DELETE CASCADE restriction in db

}