package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class SurveyDeploy implements Table {

	protected int id = -1;
	protected int survey_form;
	protected boolean ended = false;
	protected Timestamp start;
	protected Timestamp end;
	
	public static SelectBuilder<SurveyDeploy> SELECT() {
		return new SelectBuilder<SurveyDeploy>("survey_deploys", SurveyDeploy.class);
	}
	
	public int getId() { return this.id; }
	public int getSurveyForm() { return this.survey_form; }
	public boolean getEnded() { return this.ended; }
	public Timestamp getStart() { return this.start; }
	public Timestamp getEnd() { return this.end; }
	
	@Override
	public void set(String field, Object value) {
		switch (field) {
		case "id":
			this.id = ((Integer)value).intValue();
			break;
		case "survey_form":
			this.survey_form = ((Integer)value).intValue();
			break;
		case "ended":
			this.ended = ((Boolean)value).booleanValue();
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
		String q = "INSERT INTO survey_deploys (survey_form, ended, start";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (this.end != null)
			q += ", end";
		
		q += ") VALUES (" + this.survey_form + ", " + (this.ended ? 1 : 0) + ", '" + formatter.format(this.start) + "'";
		
		if (this.end != null)
			q += ", '" + formatter.format(this.end) + "'";
		
		q += ");";
		
		try {
			
			DB.beginPersistantConnection();
			
			DB.execNonQuery(q);
			ResultSet idSet = DB.execQuery("SELECT LAST_INSERT_ID();");
			idSet.next();
			this.set("id", idSet.getInt(1));
			
			DB.terminatePersistantConnection();
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void update() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		
		String q = "UPDATE survey_deploys "
				 + "SET survey_form=" + this.survey_form + ", start='" + formatter.format(this.start) + "'";
		
		if (this.end != null)
			q += ", end='" + formatter.format(this.end) + "'";
		
		q += " WHERE id=" + this.id + ";";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}

	@Override
	public void delete() {
		String q = "DELETE FROM survey_deploys "
				 + "WHERE id=" + this.id + " AND survey_form=" + this.survey_form + ";";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}

}
