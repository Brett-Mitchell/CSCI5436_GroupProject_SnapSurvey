package main.content.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.content.authorizers.SpecificUserAuthorizer;
import main.database.SurveyForm;
import main.database.SurveyFormQuestion;

public class AddSurveyFormQuestionEndpoint extends Endpoint {
	
	public AddSurveyFormQuestionEndpoint() {
		super(new SpecificUserAuthorizer());
	}
	
	@Override
	protected void preAuthorize(HttpServletRequest request) {
		int fid = Integer.parseInt(request.getParameter("formId"));
		// Get the researcher corresponding to the found survey form
		SurveyForm sf = SurveyForm.SELECT()
								 .where("id", Integer.toString(fid))
								 .getFirst();
		// Authorize the request against the given researcher
		((SpecificUserAuthorizer)this.authorizer).setUserId(sf.getResearcher());
	}

	@Override
	public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
		int formId = Integer.parseInt(req.getParameter("formId"));
		String text = req.getParameter("text");
		//String type = req.getParameter("type");
		
		SurveyFormQuestion q = new SurveyFormQuestion();
		
		q.set("text", text);
		q.set("form", formId);
		
		q.create();
		
		return "{\"success\": true}";
	}
	
}
