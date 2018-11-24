package main.content.endpoints;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.database.Participant;
import main.database.Researcher;
import main.database.User;
import main.database.UserSession;

public class RegisterEndpoint extends Endpoint {

	@Override
	public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String accountType = req.getParameter("accountType");
		
		if (username    == null || username.isEmpty()    ||
			password    == null || password.isEmpty()    ||
			email       == null || email.isEmpty()       ||
			accountType == null || accountType.isEmpty()   ) {
			return "{\"failed\": true, \"message\": \"Missing one of: username, password, email, or account type\"}";
		}
		
		if (!Researcher.SELECT().where("username", username).get().isEmpty() ||
			!Participant.SELECT().where("username", username).get().isEmpty()  )
			return "{\"failed\": true, \"message\": \"That username already exists\"}";
		
		if (!Researcher.SELECT().where("email", email).get().isEmpty() ||
			!Participant.SELECT().where("email", email).get().isEmpty())
			return "{\"failed\": true, \"message\": \"That email is already in use\"}";
		
		User u;
		
		if (accountType.equals("r"))
			u = new Researcher();
		else if (accountType.equals("p"))
			u = new Participant();
		else
			return "{\"failed\": true, \"message\": \"Invalid account type\"}";
		
		u.set("username", username);
		u.set("password", password);
		u.set("email", email);
		
		u.create();
		
		UserSession newSession = new UserSession();
		newSession.user = u.getId();
		newSession.id = UserSession.newSessionId();
		newSession.create();
		Cookie sessionCookie = new Cookie("session", newSession.id);
		sessionCookie.setPath("/");
		res.addCookie(sessionCookie);
		
		return "{\"failed\": false, \"accountType\": \"" + accountType + "\"}";
		
	}

}
