package restapi.team3;

import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import org.json.JSONObject;

import entity.Step;
import controller.StepController;

@Path("/team3/steps/{userID}")
public class StepService{
  StepController sc = new StepController("step", "/home/jim/temp");

  @PathParam("userID")
  int userID = 0;

  @Path("/get/{stepID}")
  @GET
  public File get(@PathParam("stepID") String objectID){
    File s = sc.getFile(objectID, userID);
    return s;
  }

  @Path("/upload/{timestamp}")
  @POST
  @Produces(MediaType.TEXT_PLAIN)
  public Response upload(@PathParam("timestamp") long createdDate, final InputStream is){
    int result = sc.insertStep(is, createdDate, userID);
    result = (result == 1) ? 200 : 400;
    String res = (result == 200) ? "successfully added" : "bad request";
		return Response.status(200).entity(res)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
  }

  @Path("/all")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAll(){
    // Authentication
    List<Step> list = sc.getAll(userID);
    JSONObject jObj = new JSONObject();
    jObj.put("list", list);
    return Response.status(200).entity(jObj.toString())
    				.header("Access-Control-Allow-Origin", "*")
    				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
            .build();
  }

  // @Override
  // @Path("/delete/{uid}")
  // @DELETE
  // @Produces(MediaType.TEXT_PLAIN)
  // public Response delete(@PathParam("uid") String uid){
	// 	return Response.status(200).entity(uid)
	// 			.header("Access-Control-Allow-Origin", "*")
	// 			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
	// 			.build();
  // }
}
