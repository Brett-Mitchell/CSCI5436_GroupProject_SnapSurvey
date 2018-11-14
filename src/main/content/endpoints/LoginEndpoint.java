package main.content.endpoints;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.database.Participant;
import main.database.Researcher;
import main.database.User;
import main.database.UserSession;

public class LoginEndpoint extends Endpoint {

	@Override
	public String getApiResponse(HttpServletRequest req, HttpServletResponse res) {
		// Check for an existing user session
		UserSession s = UserSession.fromSessionCookie(req);
		if (s != null) {
			if (!s.expired()) {
				s.update();
				User user = s.getUser();
				String userType = "r";
				if (user instanceof Participant)
					userType = "p";
				
				return "{\"userType\": \"" + userType + "\"}";
			} else {
				s.delete();
			}
		}
		
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		List<User> userList = (List<User>)((List<?>)Researcher.SELECT().where("username", username).get());
		String userType = "r";
		if (userList.isEmpty()) {
			userList = (List<User>)((List<?>)Participant.SELECT().where("username", username).get());
			userType = "p";
		}
		
		User user = userList.get(0);
		user.password = password;
		if (user.authenticate()) {
			// If the user has an expired session, delete that before creating the new session
			List<UserSession> existingSessions = UserSession.SELECT().where("user", Integer.toString(user.getId())).get();
			if (!existingSessions.isEmpty())
				existingSessions.get(0).delete();

			// Create a new session for the current user
			UserSession newSession = new UserSession();
			newSession.user = user.getId();
			newSession.id = UserSession.newSessionId();
			newSession.create();
			Cookie sessionCookie = new Cookie("session", newSession.id);
			sessionCookie.setPath("/");
			res.addCookie(sessionCookie);
			return "{\"userType\": \"" + userType + "\"}";
		}
		
		return "{\"failed\": true}";
	}

}
