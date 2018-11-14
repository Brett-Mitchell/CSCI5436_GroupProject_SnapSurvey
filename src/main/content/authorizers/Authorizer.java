package main.content.authorizers;

import javax.servlet.http.HttpServletRequest;

public abstract class Authorizer {
	public abstract boolean auth(HttpServletRequest req);
}
