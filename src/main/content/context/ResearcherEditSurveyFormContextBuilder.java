package main.content.context;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import main.content.ContentServlet;
import main.database.SurveyForm;
import main.database.SurveyFormQuestion;
import main.database.SurveyFormQuestionChoice;

public class ResearcherEditSurveyFormContextBuilder extends ContentServlet.PageContextBuilder {

	@Override
	public HashMap<String, Object> getContext(HttpServletRequest req) {
		String formId = req.getParameter("id");
		
		SurveyForm form = SurveyForm.SELECT()
									.where("id", formId)
									.get()
									.get(0);
		List<SurveyFormQuestion> questions = SurveyFormQuestion.SELECT()
															   .where("form", formId)
															   .orderBy("id")
															   .get();
		
		String[] questionIds = new String[questions.size()];
		for (int i = 0; i < questions.size(); i++)
			questionIds[i] = Integer.toString(questions.get(i).getId());

		List<SurveyFormQuestionChoice> choices = SurveyFormQuestionChoice.SELECT()
																		 .whereIn("question", questionIds)
																		 .get();
		
		HashMap<String, Object> context = new HashMap<String, Object>();
		context.put("survey_form", form);
		context.put("questions", questions);
		context.put("choices", choices);
		
		return context;
	}
}
