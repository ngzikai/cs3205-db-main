package filter;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

@Provider
public class BasicAuthFilter implements ContainerRequestFilter {
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
	
	private static final String TEAM1_PREFIX = "team1/";
	private static final String TEAM2_PREFIX = "team2/";
	private static final String TEAM3_PREFIX = "team3/";
	
	private static final String TEAM1_USER = "team1";
	private static final String TEAM1_PASSWORD = "team1LovesCorgi";
	
	private static final String TEAM2_USER = "team2";
	private static final String TEAM2_PASSWORD = "team2LovesBananas";
	
	private static final String TEAM3_USER = "team3";
	private static final String TEAM3_PASSWORD = "team3LovesCoconut";

	

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		List<String> auth = requestContext.getHeaders().get(AUTHORIZATION_HEADER);
		
		if(auth == null) {
			//IF NO BASIC AUTH

			Response unauthorized = Response.status(Response.Status.UNAUTHORIZED).entity("Please check if you have filled in Basic Auth").build();
			requestContext.abortWith(unauthorized);
			

		}else if(!auth.isEmpty()) {
			
			String authToken = auth.get(0);
			authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
			
			String decodedString = Base64.decodeAsString(authToken);
			StringTokenizer tok = new StringTokenizer(decodedString, ":");
			String username = tok.nextToken();
			String password = tok.nextToken();
			
//			System.out.println("Username: " + username);
//			System.out.println("Password: " + password);
//			System.out.println(requestContext.getUriInfo().getPath());
			
			if(requestContext.getUriInfo().getPath().contains(TEAM1_PREFIX)) {
				
				if(username.equals(TEAM1_USER) && password.equals(TEAM1_PASSWORD)) {
					return;
				}else {
					Response unauthorized = Response.status(Response.Status.UNAUTHORIZED).entity("NO PERMISSION TO ACCESS TEAM 1").build();
					requestContext.abortWith(unauthorized);
				}
				
			} else if(requestContext.getUriInfo().getPath().contains(TEAM2_PREFIX)) {
			
				if(username.equals(TEAM2_USER) && password.equals(TEAM2_PASSWORD)) {
					return;
				}else {
					Response unauthorized = Response.status(Response.Status.UNAUTHORIZED).entity("NO PERMISSION TO ACCESS TEAM 2").build();
					requestContext.abortWith(unauthorized);
				}
				
			} else if (requestContext.getUriInfo().getPath().contains(TEAM3_PREFIX)) {
				
				if(username.equals(TEAM3_USER) && password.equals(TEAM3_PASSWORD)) {
					return;
				}else {
					Response unauthorized = Response.status(Response.Status.UNAUTHORIZED).entity("NO PERMISSION TO ACCESS TEAM 3").build();
					requestContext.abortWith(unauthorized);
				}
				
			} 
			else {
				Response noContent = Response.noContent().build();
				requestContext.abortWith(noContent);	
			}
		}
		
	}

}
