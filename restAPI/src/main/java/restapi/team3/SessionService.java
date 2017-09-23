package restapi.team3;

import utils.db.core.*;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import controller.SessionController;

@Path("/session")
public class SessionService{
  private SessionController sc = null;

  @Path("/{type}")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String get(@PathParam("type") String type){
    sc = new SessionController(type);
    List<DataObject> list = sc.getAllObjects();
    return "test";
  }
}
