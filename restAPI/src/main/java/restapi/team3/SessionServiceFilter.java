package restapi.team3;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.annotation.security.*;
import controller.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import entity.*;

import utils.SystemConfig;
import utils.exceptions.WebException;
import org.json.JSONObject;

@Path("/team3")
@RolesAllowed({"team3", "team1", "team2"})
public class SessionServiceFilter{

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
    return new SessionService(type, userID);
  }

  @Path("/user")
  public UserService filter(@QueryParam("username")String username){
    UserController uc = new UserController();
    JSONObject user = uc.getUser2(username);
    if (user == null) {
      throw new WebException(401, "Invalid user.");
    }
    user.put("username", username);
    return new UserService(user);
  }

}
