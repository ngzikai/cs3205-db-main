package restapi.team2;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import controller.TimeseriesController;
import entity.SearchResult;

@Path("team2/timeseries")
@Consumes(MediaType.APPLICATION_JSON)
public class TimeseriesService {
	
	TimeseriesController tc = new TimeseriesController();
	
	@POST
	public Response getTimeseries(SearchResult user) {
		Response response = tc.getTimeseries(user.getUid()); 
		
		if(response != null) {
			return response;
		}else {
			return Response.ok("[]", MediaType.APPLICATION_JSON).header("content-disposition", "attachment; filename = " + user.getUid() +".json").build();
		}
	
	}

}
