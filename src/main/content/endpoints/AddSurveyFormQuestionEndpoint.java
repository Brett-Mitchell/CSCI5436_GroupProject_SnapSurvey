package main.content.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.content.authorizers.ResearcherAuthorizer;
import main.database.SurveyFormQuestion;

public class AddSurveyFormQuestionEndpoint extends Endpoint {
	
	public AddSurveyFormQuestionEndpoint() {
		super(new ResearcherAuthorizer());
	}

	@Override
	public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
		int formId = Integer.parseInt(req.getParameter("formId"));
		String text = req.getParameter("text");
		String type = req.getParameter("type");
		
		SurveyFormQuestion q = new SurveyFormQuestion();
		
		q.text = text;
		q.form = formId;
		
		q.create();
		
		System.out.println(q.text + " : " + q.form);
		
		return "{\"success\": true}";
	}

}
