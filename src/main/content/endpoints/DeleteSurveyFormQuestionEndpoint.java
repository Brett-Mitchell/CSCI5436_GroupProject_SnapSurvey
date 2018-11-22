package main.content.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.content.authorizers.SpecificUserAuthorizer;
import main.database.SurveyForm;
import main.database.SurveyFormQuestion;

public class DeleteSurveyFormQuestionEndpoint extends Endpoint {
	
	public DeleteSurveyFormQuestionEndpoint() {
		super(new SpecificUserAuthorizer());
	}
	
	@Override
	protected void preAuthorize(HttpServletRequest request) {
		int qid = Integer.parseInt(request.getParameter("id"));
		// Get the survey form corresponding to the given question
		SurveyForm sf = SurveyForm.SELECT()
								  .joinOn("survey_form_questions", new String[]{"survey_form_questions.form", "survey_forms.id"})
								  .where("survey_form_questions", "id", Integer.toString(qid))
								  .getFirst();
		// Authorize the request against the given researcher
		((SpecificUserAuthorizer)this.authorizer).setUserId(sf.getResearcher());
	}

	@Override
	public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
		String questionID = req.getParameter("id");
		if (questionID == null) return "{\"success\": false}";
		SurveyFormQuestion question = SurveyFormQuestion.SELECT().where("id", questionID).getFirst();
		if (question != null) {
			question.delete();
			return "{\"success\": true}";
		}
		return "{\"success\": false}";
	}

}
