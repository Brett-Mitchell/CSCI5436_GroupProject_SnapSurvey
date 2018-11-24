package main.database;

import java.sql.SQLException;

public class SurveyFormQuestionChoice implements Table {
	
	protected int _id = -1;
	protected int _question;
	public String text;
	
	public static SelectBuilder<SurveyFormQuestionChoice> SELECT() {
		return new SelectBuilder<SurveyFormQuestionChoice>("survey_form_question_choices", SurveyFormQuestionChoice.class);
	}
	
	public SurveyFormQuestionChoice() {}

	/*public SurveyFormQuestionChoice(int id, int question, String text) {
		this._id = id;
		this._question = question;
		this.text = text;
	}

	public SurveyFormQuestionChoice(int question, String text) {
		this._question = question;
		this.text = text;
	}

	public SurveyFormQuestionChoice(SurveyFormQuestion question, String text) {
		this._question = question.getId();
		this.text = text;
	}*/
	
	public int getQuestion() {
		return this._question;
	}
	
	public String getText() {
		return this.text;
	}
	
	public int getId() {
		return this._id;
	}
	
	public void create() {
		String q = "INSERT INTO survey_form_question_choices (question, text) "
				 + "VALUES ('" + this._question + "','" + this.text + "');";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}
	
	public void update() {
		String q = "UPDATE survey_form_question_choices "
				 + "SET question=" + this._question + ", `text`='" + this.text + "' "
				 + "WHERE id=" + this._id + ";";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}
	
	public void delete() {
		String q = "DELETE FROM survey_form_question_choices "
				 + "WHERE id=" + this._id + " AND question=" + this._question + ";";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}

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
