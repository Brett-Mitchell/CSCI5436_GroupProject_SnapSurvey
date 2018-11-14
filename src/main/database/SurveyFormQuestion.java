package main.database;

import java.sql.SQLException;
import java.util.List;

public class SurveyFormQuestion implements Table {
	
	protected int _id = -1;
	public int form;
	public String text;
	private List<SurveyFormQuestionChoice> choices;
	
	public static SelectBuilder<SurveyFormQuestion> SELECT() {
		return new SelectBuilder<SurveyFormQuestion>("survey_form_questions", SurveyFormQuestion.class);
	}
	
	public void retrieveChoices() {
		this.choices = SurveyFormQuestionChoice.SELECT()
				.where("question", Integer.toString(this._id))
				.get();
	}
	
	public List<SurveyFormQuestionChoice> getChoices() {
		return this.choices;
	}
	
	public int getId() { return this._id; }
	public String getText() { return this.text; }
	
	public SurveyFormQuestion() {}
	
	/*public SurveyFormQuestion(int id, int form, String text) {
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
		this.form = form.getId();
		this.text = text;
	}

	public SurveyFormQuestion(SurveyForm form, String text) {
		this.form = form.getId();
		this.text = text;
	}*/
	
	public void create() {
		String q = "INSERT INTO survey_form_questions (form, `text`) "
				 + "VALUES (" + this.form + ", '" + this.text + "');";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void update() {
		String q = "UPDATE survey_form_questions "
				 + "SET form=" + this.form + ", `text`='" + this.text + "' "
		 		 + "WHERE id=" + this._id + ";";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}
	
	public void delete() {
		String q = "DELETE FROM survey_form_questions ";
		
		if (this._id != -1)
			q += "WHERE id=" + this._id + ";";
		else
			q += "WHERE form=" + this.form + " AND `text`='" + this.text + "';";
		
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
		case "form":
			this.form = ((Integer)value).intValue();
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
