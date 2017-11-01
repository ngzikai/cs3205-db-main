package restapi.team3;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import controller.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.sql.Timestamp;
import entity.*;
import entity.steps.*;
import utils.team3.CommonUtil;

import org.json.JSONObject;
import com.google.gson.*;

import utils.SystemConfig;
import utils.GUID;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


public class SessionService{

	private String fileDirectory = SystemConfig.getConfig("storage_directory");
	private int userID;
	SessionController sc = null;
	private String type = "";

	public SessionService(){
		 sc = new SessionController();
	}

	public SessionService(String type, int userID){
		sc = new SessionController();
		this.userID = userID;
		this.type = type;
	}

	@Path("/get/{uid}/meta")
	@GET
	@Produces("application/octet-stream")
	public Response getMeta(@PathParam("uid") int uid){
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

		return Response.status(500).entity("Server error, contact the administrator.").build();
	}

	@Path("/get/{uid}")
	@GET
	@Produces("application/octet-stream")
	public Response get(@PathParam("uid") int uid){
		File file = null;
		Data data = null;
		data = sc.get(userID, uid);

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

	@Path("/all")
	@GET
	@Produces("application/json")
	public Response getAll(){
		// Authentication
		List<Data> dataList = null;
		dataList = sc.getAll(userID, type);

		if(dataList == null){
			return Response.status(400).entity("Invalid request.").build();
		}

		JSONObject jObj = new JSONObject();
		jObj.put("list", dataList);
		return Response.status(200).entity(jObj.toString())
						.header("Access-Control-Allow-Origin", "*")
						.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
						.build();
	}

	@Path("/upload/{timestamp}")
	@POST
	@Produces("application/octet-stream")
	public Response insert(@PathParam("timestamp") long createdDate, InputStream inputstream){
		if(type.equalsIgnoreCase("heart")){
			int heartrate = -1;
			try{
				BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
				heartrate = Integer.parseInt(br.readLine());
			}catch(Exception e){
				return Response.status(400).entity("heartrate is not an integer.").build();
			}
			Data data = new Data(-1, userID, "Heart Rate", type,
													 "Heartrate of user: " + userID,
													 new Timestamp(createdDate),
													 new Timestamp(System.currentTimeMillis()),
													 String.valueOf(heartrate));
			int result = sc.insert(data);
			if(result == 1){
				return Response.status(200).entity("successfully added heartrate: " + data.getContent()).build();
			}
		} else if (type.equalsIgnoreCase("video") || type.equalsIgnoreCase("image")){
			String mediaExt = (type.equalsIgnoreCase("video")) ? ".mp4" : ".jpeg";
			if(mediaExt.equalsIgnoreCase("unknown")){
				return Response.status(400).entity("unknown type request").build();
			}
			// Check if it is a video/image file
			try{
				File file = File.createTempFile("temp", ".check");
				FileOutputStream out = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int bytesRead;
				while((bytesRead=inputstream.read(buffer))!= -1){
					out.write(buffer, 0, bytesRead);
				}
				out.flush();
				String fileType = Files.probeContentType(file.toPath());
				if(!fileType.matches("^(video|image).*")){
					out.close();
					file.delete();
					return Response.status(400).entity("Not a correct file").build();
				}
				inputstream = new FileInputStream(file);
				out.close();
				file.delete();
			}catch(Exception e){
				e.printStackTrace();
				return Response.status(400).entity("Invalid file received.").build();
			}
			Data data = new Data(-1, userID, "File", type,
													 type + " of user: " + userID,
													 new Timestamp(createdDate),
													 new Timestamp(System.currentTimeMillis()),
													 createdDate + "_" + GUID.BASE58() + mediaExt);
			int result = sc.insert(data);
			if (result == 1){
				sc.writeToFile(inputstream, fileDirectory + "/" + data.getUid() + "/" + type +"/" + data.getContent());
				return Response.status(200).entity("successfully added "+type+": " + data.getContent()).build();
			}
		} else if (type.equalsIgnoreCase("step")){
			// Check if it is an JSON object
			try{
				byte[] content = CommonUtil.inputStreamToByteArray(inputstream);
				BufferedReader reader = sc.prepareContentToJSON(content);
				Gson gson = new GsonBuilder().create();
				Steps steps = gson.fromJson(reader, Steps.class);
				reader.close();
				Data data = new Data(-1, userID, "Time Series", type,
														 type + " of user: " + userID,
														 new Timestamp(createdDate),
														 new Timestamp(System.currentTimeMillis()),
														 createdDate + "_" + GUID.BASE58() + ".json");
				int result = sc.insert(data);
				if (result == 1){
					InputStream targetStream = new ByteArrayInputStream(content);
					sc.writeToFile(targetStream, fileDirectory + "/" + data.getUid() + "/" + type +"/" + data.getContent());
					return Response.status(200).entity("successfully added step: " + data.getContent()).build();
				} else {
					// delete the record in the database
				}
			}catch(Exception e){
				e.printStackTrace();
				return Response.status(400).entity("Not a valid Step JSON file.").build();
			}
		}
		return Response.status(400).entity("unknown type request").build();
	}
}
