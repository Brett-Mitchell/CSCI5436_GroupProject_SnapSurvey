package main.content.context;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import main.content.ContentServlet;
import main.database.SurveyForm;
import main.database.SurveyFormQuestion;
import main.database.SurveyFormQuestionChoice;

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
