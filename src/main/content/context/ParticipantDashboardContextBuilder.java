package main.content.context;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import main.database.SurveyDeploy;
import main.database.SurveyDeployInvite;
import main.database.SurveyForm;
import main.database.UserSession;

public class ParticipantDashboardContextBuilder extends ContextBuilder {

	@Override
	public HashMap<String, Object> getContext(HttpServletRequest req) {
		
		UserSession sess = UserSession.fromSessionCookie(req);
		String participantEmail = sess.getUser().getEmail();
		List<SurveyDeployInvite> invites = SurveyDeployInvite.SELECT()
															 .where("email", participantEmail)
															 .get();
		
		String[] invitedDeploys = new String[invites.size()];
		for (int i = 0; i < invites.size(); i++)
			invitedDeploys[i] = Integer.toString(invites.get(i).getSurveyDeploy());
		
		List<SurveyDeploy> deploys = SurveyDeploy.SELECT()
												 .whereIn("id", invitedDeploys)
												 .get();
		
		String[] invitedForms = new String[deploys.size()];
		for (int i = 0; i < deploys.size(); i++) 
			invitedForms[i] = Integer.toString(deploys.get(i).getSurveyForm());
		
		List<SurveyForm> surveyForms = SurveyForm.SELECT()
												 .whereIn("id", invitedForms)
												 .get();
		
		HashMap<String, Object> context = new HashMap<String, Object>();
		
		context.put("survey_forms", surveyForms);
		context.put("survey_invites", invites);
		context.put("survey_deploys", deploys);
		
		return context;
	}

}
