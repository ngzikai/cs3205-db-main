package restapi.team3;

import java.util.*;
import java.io.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

import entity.ImageVideo;
import controller.ImageVideoController;

@Path("/team3/{type}/{userID}")
public class ImageVideoService{


  @PathParam("userID")
  int userID = 0;

  @PathParam("type")
  String type = "";

  ImageVideoController ivc = null;

  @Path("/get/{fileID}")
  @GET
  public File get(@PathParam("fileID") String objectID){
    if(!type.equalsIgnoreCase("video") && !type.equalsIgnoreCase("image")){
      return null;
    }
    ivc = new ImageVideoController(type, "/home/jim/temp");
    File s = ivc.getFile(objectID, userID);
    if(s==null){
      return null;
    }
    return s;
  }

  @Path("/upload/{timestamp}")
  @POST
  @Produces(MediaType.TEXT_PLAIN)
  public Response upload(@PathParam("timestamp") long createdDate, final InputStream is){
    ivc = new ImageVideoController(type, "/home/jim/temp");
    int result = ivc.insertFile(is, createdDate, userID);
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
    System.out.println(type);
    if(!type.equalsIgnoreCase("video") && !type.equalsIgnoreCase("image")){
      return Response.status(404).build();
    }
    ivc = new ImageVideoController(type, "/home/jim/temp");
    System.out.println("how is it ra?");
    List<ImageVideo> list = ivc.getAll(userID);
    JSONObject jObj = new JSONObject();
    jObj.put("list", list);
    return Response.status(200).entity(jObj.toString())
    				.header("Access-Control-Allow-Origin", "*")
    				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
            .build();
  }
  // @Override
  // @Path("/get/{objectID}")
  // @GET
  // @Produces(MediaType.APPLICATION_JSON)
  // public Response get(@PathParam("objectID") String objectID){
  //   JSONObject jsonObject = super.sc.getSessionObject(objectID);
	// 	return Response.status(200).entity(jsonObject)
	// 			.header("Access-Control-Allow-Origin", "*")
	// 			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
	// 			.build();
  // }

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
