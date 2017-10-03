package restapi.team2;

import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import controller.SearchController;
import entity.Search;
import entity.SearchResult;

@Path("team2/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SearchService {
	SearchController sc = new SearchController();
	
	@GET
	public ArrayList<SearchResult> Search(Search search) {
		//Date startDob, Date endDob, String gender, String bloodType, String zipcode, int conditionID
		return sc.search(search);
	}
}
