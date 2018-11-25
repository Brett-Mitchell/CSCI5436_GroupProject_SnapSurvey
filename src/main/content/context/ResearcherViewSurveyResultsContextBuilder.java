package main.content.context;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import main.database.SurveyDeploy;
import main.database.SurveyForm;
import main.database.SurveyResponse;

public class ResearcherViewSurveyResultsContextBuilder extends ContextBuilder {

	@Override
	public HashMap<String, Object> getContext(HttpServletRequest req) {
		
		String deployId = req.getParameter("id");
		
		SurveyDeploy deploy = SurveyDeploy.SELECT()
										  .where("id", deployId)
										  .getFirst();
		
		String formId = Integer.toString(deploy.getSurveyForm());
		
		SurveyForm sf = SurveyForm.SELECT()
								  .where("id", formId)
								  .getFirst();
		
		sf.retrieveQuestions();
		
		List<SurveyResponse> responses = SurveyResponse.SELECT()
													   .where("survey_deploy", deployId)
													   .get();
		
		for (SurveyResponse response : responses)
			response.retrieveDetails();
		
		HashMap<String, Object> context = new HashMap<String, Object>();
		
		context.put("survey_form", sf);
		context.put("deploy", deploy);
		context.put("survey_responses", responses);
		
		return context;
	}

}
