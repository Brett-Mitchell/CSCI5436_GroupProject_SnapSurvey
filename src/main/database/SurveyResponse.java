package main.database;

import java.sql.SQLException;

public class SurveyResponse implements Table {
	
	private int _id = -1;
	private int _survey_form;
	private int _invite;
	
	public static SelectBuilder<SurveyResponse> SELECT() {
		return new SelectBuilder<SurveyResponse>("survey_responses", SurveyResponse.class);
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
		case "id":
			this._id = ((Integer)value).intValue();
			break;
		case "survey_form":
			this._survey_form = ((Integer)value).intValue();
			break;
		case "invite":
			this._invite = ((Integer)value).intValue();
			break;
		}
	}

	@Override
	public void create() {
		String q = "INSERT INTO survey_responses (survey_form, invite) "
				 + "VALUES (" + this._survey_form + ", " + this._invite + ");";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}

	@Override
	public void update() {
		String q = "UPDATE survey_responses "
				 + "SET survey_form=" + this._survey_form + ", invite=" + this._invite + " "
				 + "WHERE id=" + this._id;
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}

	@Override
	public void delete() {
		String q = "DELETE FROM survey_responses ";
		
		if (this._id != -1)
			q += "WHERE id=" + this._id + ";";
		else
			q += "WHERE survey_form=" + this._survey_form + " AND invite=" + this._invite + ";";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}

}
