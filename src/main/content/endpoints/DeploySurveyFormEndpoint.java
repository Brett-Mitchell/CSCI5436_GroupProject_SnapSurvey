package main.content.endpoints;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import main.content.authorizers.SpecificUserAuthorizer;
import main.database.SurveyDeploy;
import main.database.SurveyDeployInvite;
import main.database.SurveyForm;

public class DeploySurveyFormEndpoint extends Endpoint {

	public DeploySurveyFormEndpoint() {
		super(new SpecificUserAuthorizer());
	}
	
	@Override
	protected void preAuthorize(HttpServletRequest req) {
		String formId = req.getParameter("formId");
		
		if (formId == null) {
			((SpecificUserAuthorizer)this.authorizer).setUserId(-1);
			return;
		}
		
		SurveyForm sf = SurveyForm.SELECT()
								  .where("id", formId)
								  .getFirst();
		
		if (sf == null) {
			((SpecificUserAuthorizer)this.authorizer).setUserId(-1);
			return;
		}
		
		((SpecificUserAuthorizer)this.authorizer).setUserId(sf.getResearcher());
	}

	@Override
	public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
		String formId = req.getParameter("formId");
		String emailsStr = req.getParameter("emails");
		System.out.println(emailsStr);
		String[] emails;
		
		try {
			emails = new Gson().fromJson(emailsStr, String[].class);
		} catch (JsonSyntaxException e) {
			return "{\"success\": false, \"message\": \"Malformed email list\"}";
		}
		
		SurveyDeploy deploy = new SurveyDeploy();
		
		deploy.set("survey_form", Integer.parseInt(formId));
		deploy.set("start", new Timestamp(System.currentTimeMillis()));
		
		deploy.create();
		
		for (String email : emails) {
			System.out.println(email);
			SurveyDeployInvite invite = new SurveyDeployInvite();
			invite.set("email", email);
			invite.set("survey_deploy", deploy.getId());
			invite.create();
		}
		
		return "{\"success\": true}";
	}
	
}
