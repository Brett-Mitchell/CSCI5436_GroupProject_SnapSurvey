package main.content.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.content.authorizers.ResearcherAuthorizer;
import main.database.SurveyFormQuestion;

public class DeleteSurveyFormQuestionEndpoint extends Endpoint {
	
	public DeleteSurveyFormQuestionEndpoint() {
		super(new ResearcherAuthorizer());
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
