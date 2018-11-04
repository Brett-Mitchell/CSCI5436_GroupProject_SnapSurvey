package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SurveyFormQuestion implements Table {
	
	protected int _id = -1;
	protected int form;
	public String text;
	
	public static SurveyFormQuestion get(int form, String text) {
		return null;
	}
	
	public static SurveyFormQuestion get(int form, int id) {
		return null;
	}
	
	public SurveyFormQuestion(int id, int form, String text) {
		this._id = id;
		this.form = form;
		this.text = text;
	}

	public SurveyFormQuestion(int form, String text) {
		this.form = form;
		this.text = text;
	}

	public SurveyFormQuestion(int id, SurveyForm form, String text) {
		this._id = id;
		this.form = form.id();
		this.text = text;
	}

	public SurveyFormQuestion(SurveyForm form, String text) {
		this.form = form.id();
		this.text = text;
	}
	
	public int id() {
		return this._id;
	}
	
	public void create() {
		String q = "INSERT INTO survey_form_questions ";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}
	public void update() {}
	public void delete() {}

	@Override
	public void set(String field, Object value) {
		
	}

}
