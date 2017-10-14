package restapi.team3;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import controller.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.sql.Timestamp;
import entity.*;
import entity.sessions.*;

import org.json.JSONObject;

import utils.SystemConfig;
import utils.GUID;

@Path("/team3/{type}/{userID}")
public class SessionService{

  private String fileDirectory = SystemConfig.getConfig("storage_directory");

  @GET
  @Produces("application/json")
  public Response get(){
    return Response.status(200).entity("it is running").build();
  }

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

  @Path("/get/{uid}")
  @GET
  @Produces("application/octet-stream")
  public Response get(@PathParam("type")String type, @PathParam("userID") int userID, @PathParam("uid") int uid){
    SessionController sc = getSessionController(type);
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
  public Response getAll(@PathParam("type")String type, @PathParam("userID") int userID){
    // Authentication
    SessionController sc = getSessionController(type);
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
  public Response insert(@PathParam("type")String type, @PathParam("timestamp") long createdDate, @PathParam("userID") int userID, InputStream inputstream){
    SessionController sc = getSessionController(type);
    if(type.equalsIgnoreCase("heart")){
      int heartrate = -1;
      try{
        BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
        heartrate = Integer.parseInt(br.readLine());
      }catch(Exception e){
        return Response.status(400).entity("heartrate is not an integer.").build();
      }
      HeartRate hr = new HeartRate();
      Data data = new Data();
      data.setUid(userID);
      data.setContent(String.valueOf(heartrate));
      data.setCreationdate(new Timestamp(createdDate));
      data.setModifieddate(new Timestamp(System.currentTimeMillis()));
      data.setType("Heart Rate");
      data.setTitle("Heartrate of user: " + userID);
      data.setSubtype(type);
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
        out.close();
        inputstream = new FileInputStream(file);
        file.delete();
      }catch(Exception e){
        e.printStackTrace();
        return Response.status(400).entity("Invalid file received.").build();
      }

      Data data = new Data();
      data.setUid(userID);
      data.setContent(createdDate + "_" + GUID.BASE58() + mediaExt);
      data.setCreationdate(new Timestamp(createdDate));
      data.setModifieddate(new Timestamp(System.currentTimeMillis()));
      data.setType("File");
      data.setTitle(type + " of user: " + userID);
      data.setSubtype(type);
      int result = sc.insert(data);
      if (result == 1){
        sc.writeToFile(inputstream, fileDirectory + "/" + data.getUid() + "/" + type +"/" + data.getContent());
        return Response.status(200).entity("successfully added "+type+": " + data.getContent()).build();
      }
    } else if (type.equalsIgnoreCase("step")){
      // Check if it is an JSON object
      try{
        BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
        String jsonString = "";
        String line = "";
        while ((line=br.readLine()) != null){
          jsonString += line;
        }
        inputstream = new ByteArrayInputStream(jsonString.getBytes());
        JSONObject jsonObject = new JSONObject(jsonString);
      }catch(Exception e){
        return Response.status(400).entity("Not a JSON file.").build();
      }

      Data data = new Data();
      data.setUid(userID);
      data.setContent(createdDate + "_" + GUID.BASE58() + ".json");
      data.setCreationdate(new Timestamp(createdDate));
      data.setModifieddate(new Timestamp(System.currentTimeMillis()));
      data.setType("Time Series");
      data.setTitle(type + " of user: " + userID);
      data.setSubtype(type);
      int result = sc.insert(data);
      if (result == 1){
        sc.writeToFile(inputstream, fileDirectory + "/" + data.getUid() + "/" + type +"/" + data.getContent());
        return Response.status(200).entity("successfully added step: " + data.getContent()).build();
      }
    }
    return Response.status(400).entity("unknown type request").build();
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
}
