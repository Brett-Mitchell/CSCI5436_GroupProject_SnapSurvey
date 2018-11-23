package main.content.context;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import main.database.SurveyForm;

public class ResearcherEditSurveyFormContextBuilder extends ContextBuilder {

	@Override
	public HashMap<String, Object> getContext(HttpServletRequest req) {
		String formId = req.getParameter("id");
		
		SurveyForm form = SurveyForm.SELECT()
									.where("id", formId)
									.getFirst();
		form.retrieveQuestions();
		
		HashMap<String, Object> context = new HashMap<String, Object>();
		context.put("survey_form", form);
		
		return context;
	}
}
