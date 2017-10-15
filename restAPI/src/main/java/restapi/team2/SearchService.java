package restapi.team2;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
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
	
	@POST
	public ArrayList<SearchResult> Search(Search search) {
		return sc.search(search);
	}
}
