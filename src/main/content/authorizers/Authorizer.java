package main.content.authorizers;

import javax.servlet.http.HttpServletRequest;

import main.database.User;

public abstract class Authorizer {
	public static final class AuthorizationResult {
		private boolean authorized;
		private User user = null;
		
		public AuthorizationResult(boolean authorized, User user) {
			this.authorized = authorized;
			this.user = user;
		}
		
		public boolean Authorized() { return this.authorized; }
		public User User() { return this.user; }
	}
	
	public abstract AuthorizationResult authorize(HttpServletRequest req);
}
