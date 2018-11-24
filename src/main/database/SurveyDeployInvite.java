package main.database;

import java.sql.SQLException;

public class SurveyDeployInvite implements Table {
	
	private int _id = -1;
	private int _survey_deploy;
	public String email;
	
	public static SelectBuilder<SurveyDeployInvite> SELECT() {
		return new SelectBuilder<SurveyDeployInvite>("survey_deploy_invites", SurveyDeployInvite.class);
	}

	@Override
	public void set(String field, Object value) {
		switch (field) {
		case "id":
			this._id = ((Integer)value).intValue();
			break;
		case "survey_deploy":
			this._survey_deploy = ((Integer)value).intValue();
			break;
		case "email":
			this.email = (String)value;
			break;
		default:
			break;
		}
	}

	@Override
	public void create() {
		String q = "INSERT INTO survey_deploy_invites (survey_deploy, email) "
				 + "VALUES (" + this._survey_deploy + ", '" + this.email + "');";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void update() {
		String q = "UPDATE survey_deploy_invites "
				 + "SET survey_deploy=" + this._survey_deploy + ", email='" + this.email + "' "
				 + "WHERE id=" + this._id + ";";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}

	@Override
	public void delete() {
		String q = "DELETE FROM survey_deploy_invites ";
		
		if (this._id != -1)
			q += "WHERE id=" + this._id + ";";
		else
			q += "WHERE survey_deploy=" + this._survey_deploy + " AND email='" + this.email + "';";
		
		try {
			DB.execNonQuery(q);
		} catch(SQLException e) {}
	}

}
