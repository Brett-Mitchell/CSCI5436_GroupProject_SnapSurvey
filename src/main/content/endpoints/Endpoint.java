package main.content.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.content.authorizers.Authorizer;

public abstract class Endpoint {
	
	protected Authorizer authorizer = new Authorizer() {
		@Override
		public AuthorizationResult authorize(HttpServletRequest request) {
			return new AuthorizationResult(true, null);
		}
	};
	
	public Endpoint() {}
	
	public Endpoint(Authorizer authorizer) {
		this.authorizer = authorizer;
	}
	
	public Authorizer provideAuthorizer(HttpServletRequest request) {
		this.preAuthorize(request);
		return this.authorizer;
	}
	
	protected void preAuthorize(HttpServletRequest request) {}
	
	public abstract String getApiResponse(HttpServletRequest req, HttpServletResponse res);
}