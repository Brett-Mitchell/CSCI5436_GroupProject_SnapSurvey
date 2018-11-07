package main.database;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class SurveyDeploy implements Table {

	private int _id = -1;
	private int _survey_form;
	private boolean _ended;
	private Timestamp start;
	private Timestamp end;
	
	public static SelectBuilder<SurveyDeploy> SELECT() {
		return new SelectBuilder<SurveyDeploy>("survey_deploys", SurveyDeploy.class);
	}
	
	public int getId() { return this._id; }
	public int getSurveyForm() { return this._survey_form; }
	
	@Override
	public void set(String field, Object value) {
		switch (field) {
		case "id":
			this._id = ((Integer)value).intValue();
			break;
		case "survey_form":
			this._survey_form = ((Integer)value).intValue();
			break;
		case "ended":
			this._ended = ((Boolean)value).booleanValue();
			break;
		case "start":
			this.start = (Timestamp)value;
			break;
		case "end":
			this.end = (Timestamp)value;
			break;
		default:
			break;
		}
	}

	@Override
	public void create() {
		String q = "INSERT INTO survey_deploys (survey_form, start";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		
		if (this.end != null)
			q += ", end";
		
		q += ") VALUES (" + this._id + ", '" + formatter.format(this.start) + "'";
		
		if (this.end != null)
			q += ", '" + formatter.format(this.end) + "'";
		
		q += ");";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}

	@Override
	public void update() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		
		String q = "UPDATE survey_deploys "
				 + "SET survey_form=" + this._survey_form + ", start='" + formatter.format(this.start) + "'";
		
		if (this.end != null)
			q += ", end='" + formatter.format(this.end) + "'";
		
		q += " WHERE id=" + this._id + ";";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}

	@Override
	public void delete() {
		if (this._id != -1) {
			String q = "DELETE FROM survey_deploys "
					 + "WHERE id=" + this._id + ";";
			
			try {
				DB.execNonQuery(q);
			} catch(SQLException e) {}
		} else { /* TODO: Handle delete when no id available */ }
	}

}
