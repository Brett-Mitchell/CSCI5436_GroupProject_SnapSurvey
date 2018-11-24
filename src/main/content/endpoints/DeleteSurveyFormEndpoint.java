package main.content.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.content.authorizers.SpecificUserAuthorizer;
import main.database.SurveyForm;

public class DeleteSurveyFormEndpoint extends Endpoint {
	
	public DeleteSurveyFormEndpoint() {
		super(new SpecificUserAuthorizer());
	}
	
	@Override
	protected void preAuthorize(HttpServletRequest req) {
		String formId = req.getParameter("formId");
		
		if (formId == null) {
			((SpecificUserAuthorizer)this.authorizer).setUserId(-1);
			return;
		}
		
		SurveyForm sf = SurveyForm.SELECT().where("id", formId).getFirst();
		
		if (sf == null) {
			((SpecificUserAuthorizer)this.authorizer).setUserId(-1);
			return;
		}

		((SpecificUserAuthorizer)this.authorizer).setUserId(sf.getResearcher());
	}

	@Override
	public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
		String formId = req.getParameter("formId");
		
		if (formId == null)
			return "{\"success\": false}";
		
		SurveyForm sf = SurveyForm.SELECT().where("id", formId).getFirst();
		
		sf.delete();
		
		return "{\"success\": true}";
	}

}
