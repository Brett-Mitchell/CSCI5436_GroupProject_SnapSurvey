package main.content.authorizers;

import javax.servlet.http.HttpServletRequest;

import main.database.Researcher;
import main.database.User;
import main.database.UserSession;

public class ResearcherAuthorizer extends Authorizer {

	@Override
	public boolean authorize(HttpServletRequest req) {
		UserSession s = UserSession.fromSessionCookie(req);
		if (s == null) return false;
		User u = s.getUser();
		return u != null && u instanceof Researcher;
	}

}
