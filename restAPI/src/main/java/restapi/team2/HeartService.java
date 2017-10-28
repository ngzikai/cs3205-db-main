package restapi.team2;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import controller.HeartRateController;
import entity.HeartRate;

@Path("team2/heart")
@Produces(MediaType.APPLICATION_JSON)
public class HeartService {
	HeartRateController hc = new HeartRateController();
	
	@GET
	@Path("/{user_id}")
	public ArrayList<HeartRate> getHeartData(@PathParam("user_id") String user_id){
		return hc.getHeartRate(user_id);
	}
	

}
