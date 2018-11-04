package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SurveyFormQuestionChoice implements Table {
	
	protected int _id = -1;
	protected int _question;
	public String text;
	
	public static SurveyFormQuestionChoice get(int question, String text) {
		String q = "SELECT * FROM survey_form_question_choices "
				 + "WHERE question=" + question + " AND `text`='" + text + "';";
		
		return null;
	}
	
	public static SurveyFormQuestionChoice get(int question, int id) {
		return null;
	}
	
	public static SurveyFormQuestionChoice get(SurveyFormQuestion question, String text) {
		return SurveyFormQuestionChoice.get(question.id(), text);
	}
	
	public static SurveyFormQuestionChoice get(SurveyFormQuestion question, int id) {
		return SurveyFormQuestionChoice.get(question.id(), id);
	}

	public SurveyFormQuestionChoice(int id, int question, String text) {
		this._id = id;
		this._question = question;
		this.text = text;
	}

	public SurveyFormQuestionChoice(int question, String text) {
		this._question = question;
		this.text = text;
	}

	public SurveyFormQuestionChoice(SurveyFormQuestion question, String text) {
		this._question = question.id();
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
		switch (field) {
		case "id":
			this._id = ((Integer)value).intValue();
			break;
		case "question":
			this._question = ((Integer)value).intValue();
			break;
		case "text":
			this.text = (String)value;
			break;
		default:
			System.out.println("Field " + field + " not found.");
			break;
		}
	}
	
}
