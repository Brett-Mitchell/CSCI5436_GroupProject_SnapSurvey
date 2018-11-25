package main.content.endpoints;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import main.database.SurveyDeploy;
import main.database.SurveyDeployInvite;
import main.database.SurveyResponse;
import main.database.SurveyResponseChoiceValue;
import main.database.SurveyResponseTextValue;
import main.database.User;
import main.database.UserSession;

public class SubmitSurveyEndpoint extends Endpoint {
	
	public static class Answer {
		String answerType;
		int question;
		String[] answerValues;
		
		public Answer() {}
	}

	@Override
	public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
		String submission = req.getParameter("submission");
		String deployId = req.getParameter("deployId");
		User u = UserSession.fromSessionCookie(req).getUser();
		
		SurveyDeployInvite invite = SurveyDeployInvite.SELECT()
													  .where("email", u.getEmail())
													  .where("survey_deploy", deployId)
													  .getFirst();
		
		SurveyDeploy deploy = SurveyDeploy.SELECT()
										  .where("id", deployId)
										  .getFirst();
		
		SurveyResponse response = SurveyResponse.SELECT()
												.where("invite", Integer.toString(invite.getId()))
												.where("survey_deploy", Integer.toString(deploy.getId()))
												.getFirst();
		
		Answer[] answers = new Gson().fromJson(submission, Answer[].class);
		
		response = new SurveyResponse();
		
		response.set("survey_deploy", deploy.getId());
		response.set("invite", invite.getId());
		
		response.create();
		System.out.println(response.getId());
		
		for (Answer a : answers) {
			if (a.answerType.equals("multiple_choice")) {
				for (String value : a.answerValues) {
					SurveyResponseChoiceValue v = new SurveyResponseChoiceValue();
					v.set("survey_response", response.getId());
					v.set("question", a.question);
					v.set("choice", Integer.parseInt(value));
					v.create();
				}
			} else if (a.answerType.equals("text")) {
				SurveyResponseTextValue v = new SurveyResponseTextValue();
				v.set("survey_response", response.getId());
				v.set("question", a.question);
				v.set("value", a.answerValues[0]);
				v.create();
			}
		}
		
		invite.delete();
		
		return "";
	}

}
