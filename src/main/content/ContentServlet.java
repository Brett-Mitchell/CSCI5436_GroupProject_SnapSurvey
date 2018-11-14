package main.content;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import main.SnapSurveyConf;
import main.content.authorizers.Authorizer;
import main.content.authorizers.ParticipantAuthorizer;
import main.content.authorizers.ResearcherAuthorizer;
import main.content.context.ContextBuilder;
import main.content.context.ResearcherDashboardContextBuilder;
import main.content.context.ResearcherEditSurveyFormContextBuilder;
import main.database.UserSession;

/**
 * Servlet implementation class ContentServlet
 */
@WebServlet("/ContentServlet")
public class ContentServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private Configuration cfg;
	private String templateDir = "/WEB-INF/templates";
	private String servletUrl = SnapSurveyConf.HOSTNAME + "/content";
	
	// Internal utility class for containing related ftl files and their context builders
	private static class PageEntry {
		public String fileName;
		public ContextBuilder contextBuilder = new ContextBuilder() {
			@Override
			public HashMap<String, Object> getContext(HttpServletRequest request) {
				return new HashMap<String, Object>();
			}
		};
		public Authorizer authorizer = new Authorizer()
		{
		    @Override
			public boolean authorize(HttpServletRequest req) { return true; }
		};
		
		PageEntry(String p_fileName) {
			fileName = p_fileName;
		}
		
		PageEntry(String p_fileName, ContextBuilder p_contextBuilder) {
			fileName = p_fileName;
			contextBuilder = p_contextBuilder;
		}
		
		PageEntry(String p_fileName, ContextBuilder p_contextBuilder, Authorizer p_authorizer) {
			fileName = p_fileName;
			contextBuilder = p_contextBuilder;
			authorizer = p_authorizer;
		}
	}
	
	// Create a static map of the webpages available to users of SnapSurvey
	// Each entry has a function to build the FTL context from a request and
	// a string containing the filename of the ftl file to render and return.
	private static final Map<String, PageEntry> pages = new HashMap<String, PageEntry>();
	static 
	{
		//   .put("mapped-url", new PageEntry("file-name", PageContextBuilder /* override getContext */))
		pages.put("/home-page", new PageEntry("home-page.ftl"));
		pages.put("/login", new PageEntry("login.ftl"));
		pages.put("/register", new PageEntry("register.ftl"));
		pages.put("/r/dashboard", new PageEntry("researcher-dashboard.ftl",
												new ResearcherDashboardContextBuilder(),
												new ResearcherAuthorizer()));
		pages.put("/r/edit-survey", new PageEntry("researcher-edit-survey-form.ftl",
												  new ResearcherEditSurveyFormContextBuilder(),
												  new ResearcherAuthorizer()));
		pages.put("/p/dashboard", new PageEntry(
				"participant-dashboard.ftl",
				new ContextBuilder() {
					public HashMap<String, Object> getContext(HttpServletRequest req) {
						return new HashMap<String, Object>();
					}
				}, new ParticipantAuthorizer()));
		pages.put("/p/take-survey", new PageEntry(
				"take-survey.ftl",
				new ContextBuilder() {
					public HashMap<String, Object> getContext(HttpServletRequest req) {
						return new HashMap<String, Object>();
					}
				}, new ParticipantAuthorizer()));
	}
	
    public ContentServlet() {
        super();
    }
    
    // Initialize with freemarker configuration options
    public void init(ServletConfig scfg) throws ServletException {
    	super.init(scfg);
    	
    	cfg = new Configuration(Configuration.VERSION_2_3_28);
    	
    	File realTemplateDir = new File(getServletContext().getRealPath(templateDir));
    	
    	try {
    		cfg.setDirectoryForTemplateLoading(realTemplateDir);
    	} catch (IOException e) {}
    	
    	cfg.setDefaultEncoding("UTF-8");
    	
    	cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    	
    	cfg.setLogTemplateExceptions(false);
    	
    	cfg.setWrapUncheckedExceptions(true);
    }

    // This servlet provides html content based on the url given. Which
    // ftl template is used is determined by the Pages map
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURL().toString().substring(servletUrl.length());
		Template t = null;
		
		try {
			if (pages.containsKey(url)) {
				PageEntry pe = pages.get(url);
				// Exit if the given session is not authorized for the requested page
				if (!pe.authorizer.authorize(request)) {
					t = cfg.getTemplate(SnapSurveyConf.UNAUTHORIZED_TEMPLATE);
					t.process(null, response.getWriter());
				} else {
					t = cfg.getTemplate(pe.fileName);
					t.process(pe.contextBuilder.getContext(request), response.getWriter());
				}
			} else {
				t = cfg.getTemplate(SnapSurveyConf.ERROR_TEMPLATE);
				t.process(null, response.getWriter());
			}
		} catch (TemplateException e) {
			System.out.println(e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
