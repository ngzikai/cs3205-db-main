// package restapi.team3;
//
// import javax.ws.rs.*;
// import javax.ws.rs.core.Response;
// import controller.*;
// import java.util.*;
// import java.io.*;
// import java.nio.file.Files;
// import entity.*;
// import entity.sessions.*;
//
// import utils.exceptions.WebException;
//
// @Path("/team3")
// public class SessionServiceFilter{
//
//   @GET
//   public SessionService filterGET(@HeaderParam("Authorization")final String verificationToken){
//     // if (verificationToken.isEmpty()){
//     //   throw new WebException(401, "Missing Authorization Header");
//     // }
//     // if(!verificationToken.equalsIgnoreCase("team3")){
//     //   throw new WebException(401, "No Privileges");
//     // }
//     return new SessionService();
//   }
//
//   @POST
//   public SessionService filterPOST(@HeaderParam("Authorization")final String verificationToken){
//     return filterGET(verificationToken);
//   }
// }
