package restapi.team3;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import utils.db.*;

import java.sql.*;
import utils.db.*;
import utils.GUID;

public class UserService {

  private String username;
  private String attribute;
  public UserService(){

  }
  public UserService(String username, String attribute){
    this.username = username;
    this.attribute = attribute;
  }

  @GET
  public Response getAttribute(){
    String sql = "SELECT * FROM CS3205.user WHERE username = ?";
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        String attr = rs.getString(attribute);
        if(attr != null){
          return Response.status(201).entity(attr).build();
        }
      }
    }catch(Exception e){
      e.printStackTrace();
      return Response.status(500).entity("Server internal error, probably failed SQL").build();
    }
    return Response.status(401).entity("Bad request, probably unknown attribute").build();
  }

  @GET
  @Path("/passwordandsalt")
  public Response manuallySet(){
    String salt = GUID.BASE58();
    attribute = "salt2";
    Response response = setAttribute(salt);
    if(response.getStatus() == 500 ||response.getStatus() == 401){
      return response;
    }
    String password = "password";
    attribute = "password2";
    response = setAttribute(password);
    return response;
  }

  @POST
  public Response setAttribute(String value){
    // WARNING: DANGEROUS WAY OF SETTING PREPARESTATEMENT
    String sql = "UPDATE CS3205.user SET " + attribute + " = ? WHERE username = ?";
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, value);
      ps.setString(2, username);
      int result = ps.executeUpdate();
      System.out.println("result: "+result);
      if(result == 1){
        return Response.status(201).entity("successfully added "+attribute + " to "+username).build();
      }
    }catch(Exception e){
      e.printStackTrace();
      return Response.status(500).entity("Server internal error, probably failed SQL.").build();
    }
    return Response.status(401).entity("Probably unknown attribute. Check attribute and body").build();
  }
}
