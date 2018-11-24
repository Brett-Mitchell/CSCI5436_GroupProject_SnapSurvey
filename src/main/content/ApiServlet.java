package main.content;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.SnapSurveyConf;
import main.content.authorizers.Authorizer.AuthorizationResult;
import main.content.endpoints.*;

/**
 * Servlet implementation class ApiServlet
 */
@WebServlet("/ApiServlet")
public class ApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String servletUrl = SnapSurveyConf.HOSTNAME + "/api";
	
	// Create a static map of the webpages available to users of SnapSurvey
	// Each entry has a function to build the FTL context from a request and
	// a string containing the filename of the ftl file to render and return.
	private static final Map<String, Endpoint> endpoints = new HashMap<String, Endpoint>();
	static 
	{
		//   	 .put("mapped-url", new Endpoint() { /* Implement getApiResponse */ })
		endpoints.put("/login", new LoginEndpoint());
		endpoints.put("/add-survey-form", new AddSurveyFormEndpoint());
		endpoints.put("/delete-survey-form", new DeleteSurveyFormEndpoint());
		endpoints.put("/deploy-survey-form", new DeploySurveyFormEndpoint());
		endpoints.put("/add-survey-form-question", new AddSurveyFormQuestionEndpoint());
		endpoints.put("/update-survey-form-question", new UpdateSurveyFormQuestionEndpoint());
		endpoints.put("/delete-survey-form-question", new DeleteSurveyFormQuestionEndpoint());
		endpoints.put("/add-survey-form-question-choice", new AddSurveyFormQuestionChoiceEndpoint());
		endpoints.put("/update-survey-form-question-choice", new UpdateSurveyFormQuestionChoiceEndpoint());
		endpoints.put("/delete-survey-form-question-choice", new DeleteSurveyFormQuestionChoiceEndpoint());
	}
    
    public ApiServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURL().toString().substring(servletUrl.length());
		Endpoint endpoint = endpoints.get(url);
		
		if (endpoint == null) {
			response.getWriter().write("{\"success\": false, \"message\": \"No endpoint found at: " + url + "\"}");
			return;
		}
		
		AuthorizationResult auth = endpoint.provideAuthorizer(request).authorize(request);
		if (endpoint != null && auth.Authorized())
			response.getWriter().write(endpoint.getApiResponse(request, response));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
