package main.content;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

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

/**
 * Servlet implementation class ContentServlet
 */
@WebServlet("/ContentServlet")
public class ContentServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private Configuration cfg;
	private String templateDir = "/WEB-INF/templates";
	private String servletUrl = SnapSurveyConf.HOSTNAME + "/content";
	
	// Internal utility class for building a context map from a servlet request
	private static class PageContextBuilder {
		public HashMap<String, Object> getContext(HttpServletRequest req) {
			throw new UnsupportedOperationException("PageContextBuilder.getContext is not implemented");
		}
	}
	
	// Internal utility class for containing related ftl files and their context builders
	private static class PageEntry {
		public String fileName;
		public PageContextBuilder contextBuilder;
		
		PageEntry(String p_fileName, PageContextBuilder p_contextBuilder) {
			fileName = p_fileName;
			contextBuilder = p_contextBuilder;
		}
	}
	
	// Create a static map of the webpages available to users of SnapSurvey
	// Each entry has a function to build the FTL context from a request and
	// a string containing the filename of the ftl file to render and return.
	private static final Map<String, PageEntry> pages = new HashMap<String, PageEntry>();
	static 
	{
		//   .put("mapped-url", new PageEntry("file-name", PageContextBuilder /* override getContext */))
		pages.put("/home-page", new PageEntry(
				"home-page.ftl",
				new PageContextBuilder() {
					public HashMap<String, Object> getContext(HttpServletRequest req) {
						return new HashMap<String, Object>();
					}
				}));
		pages.put("/login", new PageEntry(
				"login.ftl",
				new PageContextBuilder() {
					public HashMap<String, Object> getContext(HttpServletRequest req) {
						return new HashMap<String, Object>();
					}
				}));
		pages.put("/register", new PageEntry(
				"register.ftl",
				new PageContextBuilder() {
					public HashMap<String, Object> getContext(HttpServletRequest req) {
						return new HashMap<String, Object>();
					}
				}));
		pages.put("/dashboard", new PageEntry(
				"dashboard.ftl",
				new PageContextBuilder() {
					public HashMap<String, Object> getContext(HttpServletRequest req) {
						return new HashMap<String, Object>();
					}
				}));
		pages.put("/take-survey", new PageEntry(
				"take-survey.ftl",
				new PageContextBuilder() {
					public HashMap<String, Object> getContext(HttpServletRequest req) {
						return new HashMap<String, Object>();
					}
				}));
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
				t = cfg.getTemplate(pages.get(url).fileName);
				t.process(pages.get(url).contextBuilder.getContext(request), response.getWriter());
			}
			else {
				t = cfg.getTemplate(SnapSurveyConf.ERROR_TEMPLATE);
				t.process(null, response.getWriter());
			}
		} catch (TemplateException e) {}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
