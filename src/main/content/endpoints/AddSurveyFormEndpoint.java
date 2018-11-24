package main.content.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.content.authorizers.ResearcherAuthorizer;
import main.database.SurveyForm;
import main.database.User;
import main.database.UserSession;

public class AddSurveyFormEndpoint extends Endpoint {
	
	public AddSurveyFormEndpoint() {
		super(new ResearcherAuthorizer());
	}

	@Override
	public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
		String title = req.getParameter("title");
		
		UserSession sess = UserSession.fromSessionCookie(req);
		
		User u = sess.getUser();
		
		SurveyForm sf = new SurveyForm();
		
		sf.set("title", title);
		sf.set("researcher", u.getId());
		
		sf.create();
		
		return "{\"success\": true, \"id\": \"" + sf.getId() + "\"}";
	}

}
