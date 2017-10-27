package restapi.team2;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import controller.FilterController;
import entity.Filter;

@Path("team2/filters")
@Produces(MediaType.APPLICATION_JSON)
public class FilterService {
	
	FilterController fc = new FilterController();
	
	@GET
	public ArrayList<Filter> getFilters(){
		return fc.getFilters();
	}
}
