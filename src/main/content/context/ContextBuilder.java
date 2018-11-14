package main.content.context;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public abstract class ContextBuilder {
	public abstract HashMap<String, Object> getContext(HttpServletRequest req);
}
