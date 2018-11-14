package main.content.context;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import main.content.ContentServlet;
import main.database.SurveyDeploy;
import main.database.SurveyForm;

public class ResearcherDashboardContextBuilder extends ContextBuilder {

	@Override
	public HashMap<String, Object> getContext(HttpServletRequest req) {
		List<SurveyForm> surveyForms = SurveyForm.SELECT().limit(5)
														  .get();
		List<SurveyDeploy> currentSurveyDeploys = SurveyDeploy.SELECT()
				  											  .where("ended", "0")
															  .limit(5)
															  .get();
		
		HashMap<String, Object> context = new HashMap<String, Object>();
		context.put("survey_forms", surveyForms);
		context.put("current_survey_deploys", currentSurveyDeploys);
		
		return context;
	}

}
