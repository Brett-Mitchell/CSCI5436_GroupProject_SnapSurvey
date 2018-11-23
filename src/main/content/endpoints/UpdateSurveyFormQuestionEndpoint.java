package main.content.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.content.authorizers.SpecificUserAuthorizer;
import main.database.SurveyForm;
import main.database.SurveyFormQuestion;

public class UpdateSurveyFormQuestionEndpoint extends Endpoint {
	
	public UpdateSurveyFormQuestionEndpoint() {
		super(new SpecificUserAuthorizer());
	}
	
	@Override
	protected void preAuthorize(HttpServletRequest req) {
		String formId = req.getParameter("formId");
		
		SurveyForm sf = SurveyForm.SELECT().where("id", formId).getFirst();
		
		((SpecificUserAuthorizer)this.authorizer).setUserId(sf.getResearcher());
	}

	@Override
	public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
		String formId = req.getParameter("formId");
		String questionId = req.getParameter("questionId");
		String text = req.getParameter("text");
		
		if (formId     == null ||
			questionId == null ||
			text 	   == null   )
			return "{\"success\": false}";
		
		SurveyFormQuestion q = SurveyFormQuestion.SELECT()
												 .where("id", questionId)
												 .where("form", formId)
												 .getFirst();
		
		if (q == null)
			return "{\"success\": false}";
		
		q.set("text", text);
		
		q.update();
		
		return "{\"success\": true}";
	}

}
