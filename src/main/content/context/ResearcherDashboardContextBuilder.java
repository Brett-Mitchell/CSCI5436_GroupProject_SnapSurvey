package main.content.context;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import main.database.SurveyDeploy;
import main.database.SurveyForm;
import main.database.UserSession;

public class ResearcherDashboardContextBuilder extends ContextBuilder {

	@Override
	public HashMap<String, Object> getContext(HttpServletRequest req) {
		UserSession session = UserSession.fromSessionCookie(req);
		String researcherId = Integer.toString(session.user);
		List<SurveyForm> surveyForms = SurveyForm.SELECT().where("researcher", researcherId)
														  .get();
		
		String[] formIds = new String[surveyForms.size()];
		
		for (int i = 0; i < surveyForms.size(); i++)
			formIds[i] = Integer.toString(surveyForms.get(i).getId());
		
		List<SurveyDeploy> currentSurveyDeploys = SurveyDeploy.SELECT()
				  											  .whereIn("survey_form", formIds)
															  .get();
		
		HashMap<String, Object> context = new HashMap<String, Object>();
		context.put("survey_forms", surveyForms);
		context.put("current_survey_deploys", currentSurveyDeploys);
		
		return context;
	}

}
