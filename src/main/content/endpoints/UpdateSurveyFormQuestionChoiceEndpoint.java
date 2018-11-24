package main.content.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.content.authorizers.SpecificUserAuthorizer;
import main.database.SurveyForm;
import main.database.SurveyFormQuestionChoice;

public class UpdateSurveyFormQuestionChoiceEndpoint extends Endpoint {

	public UpdateSurveyFormQuestionChoiceEndpoint() {
		super(new SpecificUserAuthorizer());
	}
	
	@Override
	protected void preAuthorize(HttpServletRequest req) {
		String qId = req.getParameter("questionId");
		
		SurveyForm sf = SurveyForm.SELECT()
								  .joinOn("survey_form_questions", new String[] {"survey_form_questions.form", "survey_forms.id"})
								  .where("survey_form_questions", "id", qId)
								  .getFirst();
		
		((SpecificUserAuthorizer)this.authorizer).setUserId(sf.getResearcher());
	}
	
	@Override
	public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
		String qId = req.getParameter("questionId");
		String cId = req.getParameter("choiceId");
		String text = req.getParameter("text");
		
		if (qId  == null ||
			cId  == null ||
			text == null   )
			
			return "{\"success\": false}";
		
		SurveyFormQuestionChoice c = SurveyFormQuestionChoice.SELECT()
															 .where("question", qId)
															 .where("id", "cId")
															 .getFirst();
		
		if (c == null)
			return "{\"success\": false}";
		
		c.set("text", text);
		
		c.update();
		
		return "{\"success\": false}";
	}

}
