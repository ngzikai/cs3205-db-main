package restapi.team3;

// import utils.db.core.*;
// import utils.db.*;
// import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

import controller.SessionController;

// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.ArrayList;
//
// import java.sql.Connection;
// import entity.Admin;

@Path("/steps")
public class StepService extends SessionService{

  public StepService(){
    super("step");
  }

  @Override
  @Path("/get/{uid}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response get(@PathParam("uid") String uid){
    JSONObject jsonObject = super.sc.getSessionObject(uid);
		return Response.status(200).entity(jsonObject)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
  }

  // @Override
  // @Path("/upload/{userID}")
  // @POST
  // @Produces(MediaType.TEXT_PLAIN)
  // public Response upload(@PathParam("userID") String userID){
  //
	// 	return Response.status(200).entity()
	// 			.header("Access-Control-Allow-Origin", "*")
	// 			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
	// 			.build();
  // }
  
  @Override
  @Path("/delete/{uid}")
  @DELETE
  @Produces(MediaType.TEXT_PLAIN)
  public Response delete(@PathParam("uid") String uid){
		return Response.status(200).entity(uid)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
  }
}
