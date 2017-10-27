package restapi.team2;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import entity.Filter;

@Path("/filter")
@Produces(MediaType.APPLICATION_JSON)
public class FilterService {
	
	@GET
	public ArrayList<Filter> getFilters(){
		return null;
	}
}
