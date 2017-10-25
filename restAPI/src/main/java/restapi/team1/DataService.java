package restapi.team1;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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

@Path("/team1/record/")
public class DataService {
	private String fileDirectory = SystemConfig.getConfig("storage_directory");
	DocumentController dc = new DocumentController();

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
	@Produces(MediaType.APPLICATION_JSON)
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

	//To get the file over with the rid
	@Path("/get/{rid}")
	@GET
	@Produces( {MediaType.APPLICATION_JSON, "application/octet-stream" })
	public Response get(@PathParam("rid") int rid){
		SessionController sc = new SessionController();
		File file = null;
		Data data = null;
		data = sc.get(rid);

		if(data == null){
			return Response.status(400).entity("Invalid request.").build();
		}

		JSONObject jObj = new JSONObject(data);
		if(jObj != null && data.getType().equalsIgnoreCase("Heart Rate")){
			return createResponse(jObj);
		} else {
			file = sc.getActualFile(data.getAbsolutePath());
		}
		
		String fileType = processFileType(data);
		if(fileType.equals(DocumentController.SUBTYPE) ) {
			jObj = dc.getDocument(data);
			return createResponse(jObj);
		}
		if(file != null){
			return Response.ok(file, "application/octet-stream")
					.header("X-File-Format", data.getContent().substring(data.getContent().lastIndexOf('.') + 1))
					.header("X-File-Type", fileType)
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
					.header("ALLOW", "GET, POST, DELETE, PUT, OPTIONS")
					.build();
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
	
	private String processFileType(Data data) {
		if(data.getSubtype()!= null)
			return data.getSubtype();
		
		return data.getType();
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
	
	//Retrieves a particular records
	@Path("/{rid}")
	@GET
	@Produces( {"application/json"})
	public Response getRecord(@PathParam("rid") int rid){
		SessionController sc = new SessionController();
		Data data = null;
		data = sc.get(rid);
		JSONObject jObj = null;
		
		if(data != null) {
			jObj = new JSONObject(data);
		}
		return createResponse(jObj);
	}


}
