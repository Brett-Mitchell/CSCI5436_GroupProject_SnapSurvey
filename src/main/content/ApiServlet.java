package main.content;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ApiServlet
 */
@WebServlet("/ApiServlet")
public class ApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	// Internal utility class for containing related ftl files and their context builders
	private static abstract class Endpoint {
		public abstract String getApiResponse(HttpServletRequest req);
	}
	
	// Create a static map of the webpages available to users of SnapSurvey
	// Each entry has a function to build the FTL context from a request and
	// a string containing the filename of the ftl file to render and return.
	private static final Map<String, Endpoint> endpoints = new HashMap<String, Endpoint>();
	static 
	{
		//   	 .put("mapped-url", new Endpoint() { /* Implement getApiResponse */ })
		endpoints.put("/login", new Endpoint() {
			public String getApiResponse(HttpServletRequest req) {
				return "hi";
			}
		});
	}
    
    public ApiServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
