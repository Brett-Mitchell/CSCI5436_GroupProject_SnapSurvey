package main.content.authorizers;

import javax.servlet.http.HttpServletRequest;

import main.database.Researcher;
import main.database.User;
import main.database.UserSession;

public class ParticipantAuthorizer extends Authorizer {

	@Override
	public boolean auth(HttpServletRequest req) {
		UserSession s = UserSession.fromSessionCookie(req);
		User u = s.getUser();
		return u instanceof Researcher;
	}

}
