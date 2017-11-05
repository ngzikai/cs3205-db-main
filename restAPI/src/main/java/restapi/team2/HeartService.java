package restapi.team2;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import controller.HeartRateController;
import entity.HeartRate;
import entity.SearchResult;
import entity.Week;

@Path("team2/heart")
@Produces(MediaType.APPLICATION_JSON)
public class HeartService {
	HeartRateController hc = new HeartRateController();
	
	@POST
	public LinkedList<Week> getHeartData(SearchResult user){
		LinkedList<Week> results = hc.getHeartRate(user.getUid());
		
		if(results != null) {
			return results;
		}else {
			return new LinkedList<Week>();
		}
	}
	

}
