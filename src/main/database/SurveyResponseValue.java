package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;

public abstract class SurveyResponseValue implements Table {
	
	protected int _id = -1;
	private int _question;
	private int _survey_response;
	
	// No SELECT getter here as this is an abstract table
	
	// Sort class
	public static class SortByQuestion implements Comparator<SurveyResponseValue> {
		@Override
		public int compare(SurveyResponseValue a, SurveyResponseValue b) {
			return a._question - b._question;
		}
	}
	
	public int getId() { return this._id; }
	public int getQuestion() { return this._question; }
	public int getSurveyResponse() { return this._survey_response; }
	
	@Override
	public void set(String field, Object value) {
		switch (field) {
		case "id":
			this._id = ((Integer)value).intValue();
			break;
		case "question":
			this._question = ((Integer)value).intValue();
			break;
		case "survey_response":
			this._survey_response = ((Integer)value).intValue();
			break;
		}
	}
	
	@Override
	public void create() {
		String q = "INSERT INTO survey_response_values (question, survey_response) "
				 + "VALUES (" + this._question + ", " + this._survey_response + ");";
		
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
		String q = "UPDATE survey_response_values "
				 + "SET question=" + this._question + ", survey_response=" + this._survey_response + " "
				 + "WHERE id=" + this._id;
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}
	
	@Override
	public void delete() {
		String q = "DELETE FROM survey_response_values ";
		
		if (this._id != -1)
			q += "WHERE id=" + this._id + ";";
		else
			q += "WHERE question=" + this._question + " AND survey_response=" + this._survey_response + ";";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}
	
	

}
