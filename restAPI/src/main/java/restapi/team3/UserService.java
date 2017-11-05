package restapi.team3;

// Java imports
import java.util.Base64;
import java.util.Arrays;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

// Jersey imports
import javax.ws.rs.Path;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

// Utils imports
import utils.GUID;
import utils.Cryptography;
import utils.db.*;
import utils.team3.*;

// Data objects and controllers
import controller.UserDataController;
import entity.UserChallenge;
import entity.UserMetaData;
import org.json.JSONObject;

public class UserService {

	private JSONObject user;
	private String attribute;
	private UserDataController udc;

	public UserService(){
		udc = new UserDataController(user);
	}

	public UserService(JSONObject user){
		this.user = user;
		udc = new UserDataController(user);
	}

	/**
	 * Create new Challenge for the user login
	 */
	@GET
	@Path("/challenge")
	public Response challenge(){
		return udc.issueChallenge("login");
	}

	/**
	 * Create new NFC Challenge for the user
	 */
	@GET
	@Path("/nfcchallenge")
	public Response nfcChallenge(){
		return udc.issueChallenge("nfc");
	}

	/**
	 * Login method for team 3
	 */
	@POST
	@Path("/login")
	public Response login(@HeaderParam("X-NFC-Response")String nfcToken, @HeaderParam("X-Password-Response")String authorizationHeader, @HeaderParam("debug")boolean debugMode){
		// read challenge token
		String[] authHeader = (authorizationHeader != null) ? authorizationHeader.split(" ") : new String[1];
		if(authHeader.length < 2){
			return Response.status(Response.Status.BAD_REQUEST)
										 .entity("Invalid Authorization Header.").build();
		}

		if (nfcToken == null) {
			return Response.status(Response.Status.BAD_REQUEST)
										 .entity("Missing NFC Response Header.").build();
		}

		// check number of login attempts
		UserMetaData umd = udc.getMetaData(user.getString("username"));
		if(umd == null){
			umd = new UserMetaData(Integer.parseInt(user.get("uid").toString()), 0, System.currentTimeMillis());
		}
		int lockAttempts = umd.getLockAttempts();
		long lastAttempt = umd.getLastAttempt();
		long currentTimeMillis = System.currentTimeMillis();
		long totalWaitingTime = ((long)(Math.pow(2, lockAttempts)*1000)+lastAttempt);
		long timeDifference = totalWaitingTime - currentTimeMillis;
		if ( timeDifference > 0){
			return Response.status(Response.Status.BAD_REQUEST)
										 .entity("LoginTimeout:"+ (timeDifference/1000))
										 .header("X-Timeout", (timeDifference/1000))
										 .build();
		}
		// if failed attempts are more than 10
		if (lockAttempts > 10){
			return Response.status(Response.Status.BAD_REQUEST)
										 .entity("Your account has been locked out due to multiple failed login attempts. Please contact the administrator.").build();
		}
		byte[] response = Base64.getDecoder().decode(authHeader[1].getBytes());
		System.out.println("Response: "+ authHeader[1]);
		UserChallenge uc = udc.getChallengeData(user.getString("username"), "login");
		if (uc == null){
			return Response.status(401).entity("No challenge found for user.").build();
		}
		byte[] challenge = Base64.getDecoder().decode(uc.getChallengeString().getBytes());
		System.out.println("Challenge: " + Base64.getEncoder().encodeToString(challenge));
		byte[] passwordHash = Base64.getDecoder().decode(user.getString("password2").getBytes());
		System.out.println("PasswordHash: "+ Base64.getEncoder().encodeToString(passwordHash));

		// remove challenge as it has been used
		udc.deleteUserChallenge(user.getString("username"), "login");

		if(debugMode || response.length == 32 && udc.validateResponse(response, challenge, passwordHash)){
			return validateNFCResponse(nfcToken);
		}
		// User failed to login, increase lock attempt
		lockAttempts++;
		udc.setLockAttempt(user.getString("username"), lockAttempts, System.currentTimeMillis());
		return Response.status(401).entity("Invalid credentials").build();
	}

	/**
	 * Verify the NFC challenge response of the user
	 */
	@POST
	@Path("/validatenfc")
	public Response validateNFCResponse(@HeaderParam("X-NFC-Response")String nfcToken){
		// Obtain the NFC challenge in the database
		UserChallenge uc = udc.getChallengeData(user.getString("username"), "nfc");
		if (uc == null){
			return Response.status(401).entity("No NFC challenge found for user.").build();
		}
		// Base64 decode
		byte[] challenge = Base64.getDecoder().decode(uc.getChallengeString().getBytes());
		System.out.println(Base64.getEncoder().encodeToString(challenge));
		// Base64 decode user's nfc response
		byte[] nfcTokenByte = Base64.getDecoder().decode(nfcToken.getBytes());
		System.out.println("NFCTOKEN: "+nfcToken);
		// Base64 decode the hash(secret) nfcid of the database
		byte[] nfcHash = Base64.getDecoder().decode(user.getString("nfcid").getBytes());
		System.out.println(user.getString("nfcid"));

		// remove challenge as it has been used
		udc.deleteUserChallenge(user.getString("username"), "nfc");

		// Validate the parameters
		if(udc.validateNFCResponse(nfcTokenByte, challenge, nfcHash)){
			return Response.status(200).entity(user.getInt("uid")).build();
		}
		return Response.status(401).entity("Invalid NFC Response.").build();
	}

	/**
	 * Retrieve the salt for the user
	 */
	@GET
	public Response getSalt(){
		String salt = user.getString("salt2");
		if(salt.isEmpty()){
			// generate new salt for the user
			salt = GUID.BASE58();
			int result = udc.setSalt(salt);
			if( result == 1){
				user.put("salt2", salt);
			} else {
				return Response.status(401).entity("No salt for the user.").build();
			}
		}
		return Response.status(200).entity(salt).build();
	}

	/**
	 * Populate the default password and new salt for the user
	 */
	@Deprecated
	@GET
	@Path("/passwordandsalt")
	public Response setPasswordAndSalt(){
		String salt = GUID.BASE58(), password = "password";
		System.out.println("salt: "+ salt);
		Response response = Response.status(401).entity("Setting password and salt failed.").build();
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] bytes = digest.digest((password+salt).getBytes()); // h(pwd + salt)
			System.out.println("h(pwd + salt):"+Base64.getEncoder().encodeToString(bytes));
			bytes = digest.digest(bytes); // h(h(pwd+salt))
			password = Base64.getEncoder().encodeToString(bytes);
			System.out.println("password: "+password);
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Internal Server Error while generating hash.").build();
		}
		JSONObject result = udc.updateUserPassword2(user.getString("username"), password, salt);
		user.put("password2", password);
		user.put("salt2", salt);
		if(result.getInt("result") == 1){
			// return back h(h(pwd+salt)) for debugging purposes
			response = Response.status(200).entity(user.getString("password2")).build();
		}
		return response;
	}

	@POST
	@Path("/setpassword")
	public Response setPassword2(@QueryParam("password") String password){
		Response response = Response.status(401).entity("Setting password failed.").build();
		if(password == null){
			return response;
		}
		byte[] passwordHash = Base64.getDecoder().decode(password.getBytes()); // h(pwd + salt)
		if (passwordHash.length < 32){
			return response;
		}
		String hashOfHashPass = "";
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] bytes = digest.digest(passwordHash); // h(h(pwd + salt))
			System.out.println("h(h(pwd + salt)):"+Base64.getEncoder().encodeToString(bytes));
			hashOfHashPass = Base64.getEncoder().encodeToString(bytes);
			System.out.println("hashOfHashPass: "+hashOfHashPass);
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Internal Server Error while generating hash.").build();
		}
		String salt = getSalt().readEntity(String.class);
		if(salt.equalsIgnoreCase("No salt for the user.")){
			return Response.status(500).entity("Internal Server Error while generating salt.").build();
		}
		JSONObject result = udc.updateUserPassword2(user.getString("username"), hashOfHashPass, salt);
		user.put("password2", hashOfHashPass);
		user.put("salt2", salt);
		if(result.getInt("result") == 1){
			response = Response.status(200).entity("Password successfully updated").build();
		}
		return response;
	}

	/**
	 * Generate a new NFC secret for the user
	 */
	@GET
	@Path("/populateNFC")
	public Response setNFC(){
		// Generate new secret
		byte[] secret = new byte[32];
		new SecureRandom().nextBytes(secret);
		System.out.println("NFC Secret: " + Base64.getEncoder().encodeToString(secret));
		// Prepare digest space
		byte[] computedResult = new byte[32];
		try{
			// Generate Hash of secret
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			computedResult = digest.digest(secret);
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Internal Server Error while computing hash of nfc.").build();
		}
		System.out.println("Base64 encoded digest: " + Base64.getEncoder().encodeToString(computedResult));
		// Store to database
		int storeResult = udc.setNFCID(Base64.getEncoder().encodeToString(computedResult));
		if(storeResult == 1){
			// send back the original value to write to card
			Cryptography crypto = Cryptography.getInstance();
			byte[] cText = crypto.encrypt(secret);
			return Response.status(201).entity(Base64.getEncoder().encodeToString(cText)).build();
		}

		return Response.status(500).entity("Server unable to generate NFC secret").build();
	}
}
