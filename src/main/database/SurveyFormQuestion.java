package main.database;

import java.sql.SQLException;
import java.util.List;

public class SurveyFormQuestion implements Table {
	
	protected int _id = -1;
	protected int form;
	protected String type;
	protected String text;
	protected List<SurveyFormQuestionChoice> choices;
	
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
	public String getType() { return this.type; }
	public String getText() { return this.text; }
	
	public SurveyFormQuestion() {}
	
	public void create() {
		String q = "INSERT INTO survey_form_questions (form, `type`, `text`) "
				 + "VALUES (" + this.form + ", '" + this.type + "', '" + this.text + "');";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void update() {
		String q = "UPDATE survey_form_questions "
				 + "SET `text`='" + this.text + "', `type`='" + this.type + "' "
		 		 + "WHERE id=" + this._id + " AND form=" + this.form + ";";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
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
		case "type":
			this.type = (String)value;
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
