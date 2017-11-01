package restapi.team1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import controller.LogController;
import entity.Log;

@Path("/team1/log")
public class LogService {
	LogController lc = new LogController();
	
	@GET
	@Produces("application/json")
	public Response getAllLog() throws JSONException {

		JSONObject jsonObject = lc.getAllLog();

		return createResponse(jsonObject);
	}
	
	@Path("/create")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response createLogPost(Log log) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject = lc.createLog(log);

		return createResponse(jsonObject);
	}
	
	public Response createResponse(JSONObject jsonObject) {
		if(jsonObject == null) {
			jsonObject = new JSONObject();
			jsonObject.put("result", false); 
			jsonObject.put("msg", "Null object");
		}
		
		return Response.status(200).entity(jsonObject.toString())
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.header("ALLOW", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
	}
}
