package restapi.team3;

import controller.HeartController;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;
import entity.HeartRate;

@Path("/team3/heartservice/{userID}")
public class HeartService{
  HeartController hc = new HeartController("heartrate");

  @PathParam("userID")
  int userID = 0;

  @Path("/{heartrate}/{timestamp}")
  @POST
  public Response insert(@PathParam("heartrate") int heartrate, @PathParam("timestamp") long createdDate){
    // Authentication
    int result = hc.insertHeartRate(userID, heartrate, createdDate);
    result = (result == 1) ? 200 : 400;
    String res = (result == 200) ? "successfully added" : "bad request";
    return Response.status(result).entity(res)
    				.header("Access-Control-Allow-Origin", "*")
    				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
            .build();
  }
  @Path("/all")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAll(){
    // Authentication
    List<HeartRate> hr = hc.getAll(userID);
    JSONObject jObj = new JSONObject();
    jObj.put("list", hr);
    return Response.status(200).entity(jObj.toString())
    				.header("Access-Control-Allow-Origin", "*")
    				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
            .build();
  }

  @Path("/{heartID}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response get(@PathParam("heartID") String heartID){
    // Authentication
    HeartRate hr = hc.get(heartID, userID);
    JSONObject jObj = new JSONObject(hr);
    return Response.status(200).entity(jObj.toString())
    				.header("Access-Control-Allow-Origin", "*")
    				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
            .build();
  }
}
