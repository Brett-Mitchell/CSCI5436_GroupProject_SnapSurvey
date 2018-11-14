package main.content;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import main.SnapSurveyConf;
import main.content.endpoints.LoginEndpoint;

/**
 * Servlet implementation class ApiServlet
 */
@WebServlet("/ApiServlet")
public class ApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String servletUrl = SnapSurveyConf.HOSTNAME + "/api";
    
	// Internal utility class for containing related ftl files and their context builders
	public static abstract class Endpoint {
		public abstract String getApiResponse(HttpServletRequest req, HttpServletResponse res);
	}
	
	// Create a static map of the webpages available to users of SnapSurvey
	// Each entry has a function to build the FTL context from a request and
	// a string containing the filename of the ftl file to render and return.
	private static final Map<String, Endpoint> endpoints = new HashMap<String, Endpoint>();
	static 
	{
		//   	 .put("mapped-url", new Endpoint() { /* Implement getApiResponse */ })
		endpoints.put("/login", new LoginEndpoint());
		endpoints.put("/delete-survey-form-question", new Endpoint() {
			public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
				Gson response = new Gson();
				return response.toString();
			}
		});
	}
    
    public ApiServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURL().toString().substring(servletUrl.length());
		System.out.println(url);
		Endpoint endpoint = this.endpoints.get(url);
		if (endpoint != null)
			response.getWriter().write(endpoint.getApiResponse(request, response));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
