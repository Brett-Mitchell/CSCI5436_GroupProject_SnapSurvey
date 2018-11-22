package main.content.authorizers;

import javax.servlet.http.HttpServletRequest;

import main.database.Researcher;
import main.database.User;
import main.database.UserSession;

public class ResearcherAuthorizer extends Authorizer {

	@Override
	public AuthorizationResult authorize(HttpServletRequest req) {
		UserSession s = UserSession.fromSessionCookie(req);
		if (s == null)
			return new AuthorizationResult(false, null);
		User u = s.getUser();
		return new AuthorizationResult(u != null && u instanceof Researcher, u);
	}

}
