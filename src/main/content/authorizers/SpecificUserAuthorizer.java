package main.content.authorizers;

import javax.servlet.http.HttpServletRequest;

import main.database.User;
import main.database.UserSession;

// Makes sure that the user-session associated with the given cookie is
// valid and that the user associated with the session is authorized to
// perform the specific action being requested
public class SpecificUserAuthorizer extends Authorizer {
	
	private int userId = -1;
	
	// Sets the user id to check against
	public SpecificUserAuthorizer setUserId(int id) {
		this.userId = id;
		return this;
	}

	@Override
	public AuthorizationResult authorize(HttpServletRequest req) {
		if (this.userId == -1)
			return new AuthorizationResult(false, null);
		UserSession s = UserSession.fromSessionCookie(req);
		if (s == null) 
			return new AuthorizationResult(false, null);
		User u = s.getUser();
		
		// Users are authorized if their account is associated with an active session
		// and their id matches the id required for the current action
		return new AuthorizationResult((u != null && u.getId() == this.userId), u);
	}
	
}
