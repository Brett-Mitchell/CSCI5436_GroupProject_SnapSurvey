package main.content.context;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import main.database.SurveyDeploy;
import main.database.SurveyForm;

public class TakeSurveyContextBuilder extends ContextBuilder {

	@Override
	public HashMap<String, Object> getContext(HttpServletRequest req) {
		String form = req.getParameter("survey");
		String deployId = req.getParameter("deploy");
		
		SurveyDeploy deploy = SurveyDeploy.SELECT().where("id", deployId).where("survey_form", form).getFirst();
		
		SurveyForm sf = SurveyForm.SELECT().where("id", form).getFirst();
		sf.retrieveQuestions();
		
		HashMap<String, Object> root = new HashMap<String, Object>();

		root.put("deploy", deploy);
		
		if (deploy != null)
			root.put("survey", sf);
		
		return root;
	}

}
