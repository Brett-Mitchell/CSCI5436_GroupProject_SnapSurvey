package main.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SurveyForm implements Table {
	
	protected int _id = -1;
	protected List<SurveyFormQuestion> questions = new ArrayList<SurveyFormQuestion>();
	protected int _researcher;
	public String title;
	
	public static SelectBuilder<SurveyForm> SELECT() {
		return new SelectBuilder<SurveyForm>("survey_forms", SurveyForm.class);
	}
	
	public SurveyForm() {}
	
	public int getId() { return this._id; }
	public List<SurveyFormQuestion> getQuestions() { return this.questions; }
	public String getTitle() { return this.title; }
	public int getResearcher() { return this._researcher; }
	
	public void retrieveQuestions() {
		this.questions = SurveyFormQuestion.SELECT()
										   .where("form", Integer.toString(this._id))
										   .get();
		
		for (SurveyFormQuestion q : this.questions)
			q.retrieveChoices();
	}
	
	public void create() {
		String q = "INSERT INTO survey_forms (researcher, title) "
				 + "VALUES ('" + this._researcher + "','" + this.title + "');";
		
		try {
			DB.execNonQuery(q);
			
			SurveyForm sf = SELECT().where("researcher", Integer.toString(this._researcher))
									.where("title", this.title)
									.getFirst();
			
			this._id = sf._id;
			
		} catch(SQLException e) {}
	}
	
	public void update() {
		String q = "UPDATE survey_forms "
				 + "SET researcher=" + this._researcher + ", title='" + this.title + "' "
				 + "WHERE id=" + this._id + ";";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}
	
	public void delete() {
		String q = "DELETE FROM survey_forms ";
		
		if (this._id != -1)
			q += "WHERE id=" + this._id + ";";
		else
			q += "WHERE researcher=" + this._researcher + " AND title='" + this.title + "';";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
		case "id":
			this._id = ((Integer)value).intValue();
			break;
		case "researcher":
			this._researcher = ((Integer)value).intValue();
			break;
		case "title":
			this.title = (String)value;
			break;
		default:
			System.out.println("Field " + field + " not found.");
			break;
		}
	}
	
}
