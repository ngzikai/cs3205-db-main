package restapi.team1;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import controller.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.sql.Timestamp;
import entity.*;

import org.json.JSONObject;

import utils.SystemConfig;
import utils.GUID;

@Path("/team1/records/")
public class DataService {
	private String fileDirectory = SystemConfig.getConfig("storage_directory");

	@GET
	@Produces("application/json")
	public Response get(){
		return Response.status(200).entity("it is running").build();
	}
/*
	@Path("/get/{uid}/meta")
	@GET
	@Produces("application/octet-stream")
	public Response getMeta(@PathParam("type")String type, @PathParam("userID") int userID, @PathParam("uid") int uid){
		SessionController sc = getSessionController(type);
		Data data = null;
		data = sc.get(userID, uid);

		if(data == null){
			return Response.status(400).entity("Invalid request.").build();
		}

		JSONObject jObj = new JSONObject(data);
		if(data.getSubtype().equalsIgnoreCase("step")){
			jObj = sc.getStepMetaInfo(data);
		}

		if(jObj != null){
			return Response.status(200).entity(jObj.toString()).build();
		}

		return Response.status(400).entity("Server error, contact the administrator.").build();
	}


*/
	@Path("/all/{uid}")
	@GET
	@Produces("application/json")
	public Response getAll(@PathParam("uid") int uid){
		// Authentication
		String type = "all";
		SessionController sc = new SessionController();
		List<Data> dataList = null;
		dataList = sc.getAllFromUser(uid);

		if(dataList == null){
			return Response.status(400).entity("Invalid request.").build();
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("records", dataList);
		return createResponse(jsonObject);
	}

	@Path("/get/{type}/{rid}")
	@GET
	@Produces("application/octet-stream")
	public Response get(@PathParam("type")String type, @PathParam("uid") int userID, @PathParam("rid") int rid){
		SessionController sc = getSessionController(type);
		File file = null;
		Data data = null;
		data = sc.get(rid);

		if(data == null){
			return Response.status(400).entity("Invalid request.").build();
		}

		JSONObject jObj = new JSONObject(data);
		if(jObj != null && data.getSubtype().equalsIgnoreCase("heart")){
			return Response.status(200).entity(jObj.toString()).build();
		} else {
			file = sc.getActualFile(data.getAbsolutePath());
		}

		if(file != null){
			return Response.ok(file, "application/octet-stream").build();
		}
		return Response.status(400).entity("Server error, contact the administrator.").build();
	}

	private SessionController getSessionController(String type){
		if(type.equalsIgnoreCase("image") || type.equalsIgnoreCase("video")){
			return new SessionController("file");
		} else if(type.equalsIgnoreCase("heart")){
			return new SessionController("heartrate");
		} else if(type.equalsIgnoreCase("step")){
			return new SessionController("step");
		}
		return null;
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
