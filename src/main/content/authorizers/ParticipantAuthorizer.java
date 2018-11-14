package main.content.authorizers;

import javax.servlet.http.HttpServletRequest;

import main.database.Participant;
import main.database.User;
import main.database.UserSession;

public class ParticipantAuthorizer extends Authorizer {

	@Override
	public boolean authorize(HttpServletRequest req) {
		UserSession s = UserSession.fromSessionCookie(req);
		if (s == null) return false;
		User u = s.getUser();
		return u instanceof Participant;
	}

}
