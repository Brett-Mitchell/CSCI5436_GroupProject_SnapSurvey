package main.content.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.content.authorizers.SpecificUserAuthorizer;
import main.database.SurveyForm;
import main.database.SurveyFormQuestionChoice;

public class AddSurveyFormQuestionChoiceEndpoint extends Endpoint {
	
	public AddSurveyFormQuestionChoiceEndpoint() {
		super(new SpecificUserAuthorizer());
	}
	
	@Override
	protected void preAuthorize(HttpServletRequest req) {
		String qId = req.getParameter("questionId");
		
		SurveyForm sf = SurveyForm.SELECT()
								  .joinOn("survey_form_questions", new String[] {"survey_form_questions.form", "survey_forms.id"} )
								  .where("survey_form_questions", "id", qId)
								  .getFirst();
		
		((SpecificUserAuthorizer)this.authorizer).setUserId(sf.getResearcher());
	}

	@Override
	public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
		String question = req.getParameter("questionId");
		String text = req.getParameter("text");
		
		if (question == null ||
			text	 == null   )
			return "{\"success\": false}";
		
		SurveyFormQuestionChoice c = new SurveyFormQuestionChoice();
		
		c.set("question", Integer.parseInt(question));
		c.set("text", text);
		
		c.create();
		
		return "{\"success\": true}";
	}

}
