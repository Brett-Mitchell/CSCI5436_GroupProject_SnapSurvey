package main.content.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.content.authorizers.SpecificUserAuthorizer;
import main.database.SurveyForm;
import main.database.SurveyFormQuestionChoice;

public class DeleteSurveyFormQuestionChoiceEndpoint extends Endpoint {

	public DeleteSurveyFormQuestionChoiceEndpoint() {
		super(new SpecificUserAuthorizer());
	}
	
	@Override
	protected void preAuthorize(HttpServletRequest request) {
		String qId = request.getParameter("questionId");
		
		SurveyForm sf = SurveyForm.SELECT()
								  .joinOn("survey_form_questions", new String[] {"survey_forms.id", "survey_form_questions.form"})
								  .where("survey_form_questions", "id", qId)
								  .getFirst();
		
		((SpecificUserAuthorizer)this.authorizer).setUserId(sf.getResearcher());
	}

	@Override
	public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
		String cId = req.getParameter("choiceId");
		String qId = req.getParameter("questionId");
		SurveyFormQuestionChoice c = SurveyFormQuestionChoice.SELECT()
															 .where("id", cId)
															 .where("question", qId)
															 .getFirst();
		
		if (c != null) {
			c.delete();
			return "{\"success\": true}";
		}

		return "{\"success\": false}";
	}

}
