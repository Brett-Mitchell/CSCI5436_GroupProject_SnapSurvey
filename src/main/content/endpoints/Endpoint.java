package main.content.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.content.authorizers.Authorizer;

public abstract class Endpoint {
	
	public Authorizer authorizer = new Authorizer() {
		@Override
		public boolean authorize(HttpServletRequest request) {
			return true;
		}
	};
	
	public Endpoint() {}
	
	public Endpoint(Authorizer authorizer) {
		this.authorizer = authorizer;
	}
	
	public abstract String getApiResponse(HttpServletRequest req, HttpServletResponse res);
}