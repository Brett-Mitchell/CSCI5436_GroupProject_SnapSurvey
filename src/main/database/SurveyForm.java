package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SurveyForm implements Table {
	
	protected int _id = -1;
	protected List<SurveyFormQuestion> questions = new ArrayList<SurveyFormQuestion>();
	protected int researcher;
	public String title;
	
	public static ResultSet get(int researcher, String title) {
		return null;
	}
	
	public static ResultSet get(int id) {
		return null;
	}
	
	public SurveyForm() {}
	
	public int id() { return this._id; }
	
	public void create() {
		String q = "INSERT INTO survey_forms ";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}
	public void update() {}
	public void delete() {}

	@Override
	public void set(String field, Object value) {
		// TODO Auto-generated method stub
		
	}
	
}
