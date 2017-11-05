package restapi.team3;

// Java imports
import javax.ws.rs.Path;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.annotation.security.RolesAllowed;
import controller.UserController;

// Utils imports
import utils.SystemConfig;
import utils.exceptions.WebException;

// Data objects
import org.json.JSONObject;

@Path("/team3")
@RolesAllowed({"team3", "team1", "team2"})
public class SessionServiceFilter{

	UserController uc = new UserController();

	@Path("/{type}/{userID}")
	public SessionService filter(@HeaderParam("X-Password-Response")final String verificationToken, @PathParam("type")String type, @PathParam("userID") int userID){
		boolean securityOn = SystemConfig.getConfig("security_on", Boolean.class);
		if(securityOn){
			if (verificationToken == null){
				throw new WebException(401, "Missing Authorization Header");
			}
			if(!verificationToken.equalsIgnoreCase("team3")){
				throw new WebException(401, "No Privileges");
			}
		}
		if(userID <= 0){
			throw new WebException(401, "Invalid user");
		}
		JSONObject user = uc.getUserWithUID(Integer.toString(userID));
		if (user == null) {
			throw new WebException(401, "Invalid user.");
		}
		return new SessionService(type, userID, user);
	}

	@Path("/user")
	public UserService filter(@QueryParam("username")String username){
		JSONObject user = uc.getUser2(username);
		if (user == null) {
			throw new WebException(401, "Invalid user.");
		}
		user.put("username", username);
		return new UserService(user);
	}

}
