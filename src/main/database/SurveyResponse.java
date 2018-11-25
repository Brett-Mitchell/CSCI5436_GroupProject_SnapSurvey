package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SurveyResponse implements Table {
	
	protected int _id = -1;
	protected int _survey_deploy;
	protected int _invite;
	protected List<SurveyResponseValue> values = new ArrayList<SurveyResponseValue>();
	
	public static SelectBuilder<SurveyResponse> SELECT() {
		return new SelectBuilder<SurveyResponse>("survey_responses", SurveyResponse.class);
	}
	
	public int getId() { return this._id; }
	public int getSurveyDeploy() { return this._survey_deploy; }
	public int getInvite() { return this._invite; }
	public List<SurveyResponseValue> getValues() { return this.values; }

	@Override
	public void set(String field, Object value) {
		switch (field) {
		case "id":
			this._id = ((Integer)value).intValue();
			break;
		case "survey_deploy":
			this._survey_deploy = ((Integer)value).intValue();
			break;
		case "invite":
			this._invite = ((Integer)value).intValue();
			break;
		}
	}
	
	public void retrieveDetails() {
		this.values.addAll(
				SurveyResponseChoiceValue.SELECT().where("survey_response_values", "survey_response", Integer.toString(this._id)).get()
			);
		this.values.addAll(
				SurveyResponseTextValue.SELECT().where("survey_response_values", "survey_response", Integer.toString(this._id)).get()
			);
		
		Collections.sort(this.values, new SurveyResponseValue.SortByQuestion());
	}

	@Override
	public void create() {
		String q = "INSERT INTO survey_responses (survey_deploy, invite) "
				 + "VALUES (" + this._survey_deploy + ", " + this._invite + ");";
		
		try {
			DB.beginPersistantConnection();
			
			DB.execNonQuery(q);
			ResultSet rs = DB.execQuery("SELECT LAST_INSERT_ID();");
			rs.next();
			this._id = rs.getInt(1);
			
			DB.terminatePersistantConnection();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void update() {
		String q = "UPDATE survey_responses "
				 + "SET survey_deploy=" + this._survey_deploy + ", invite=" + this._invite + " "
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
			q += "WHERE survey_deploy=" + this._survey_deploy + " AND invite=" + this._invite + ";";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}

}
