package cs3205.db.restapi.team2;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cs3205.db.restapi.team2.model.Researcher;
import cs3205.db.restapi.team2.model.SearchQuery;
import cs3205.db.restapi.team2.model.SearchResults;
import cs3205.db.restapi.team2.services.ResearcherService;

@Path("/researcher")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResearcherResource {
	public ResearcherService rs;
	
	@GET
	@Path("/{uid}")
	public Researcher getResearcher(@PathParam("uid") int uid) {
		return null;
	}
	
	@POST
	public Researcher addResearcher(Researcher newResearcher) {
		return null;
	}
	
	@PUT
	@Path("/{uid}")
	public Researcher updateResearcher(@PathParam("uid") int uid, Researcher researcher) {
		return null;
	}
	
	@DELETE
	@Path("/{uid}")
	public Researcher deleteResearcher(@PathParam("uid") int uid, Researcher researcher) {
		return null;
	}
	
	@GET
	@Path("/{uid}/search")
	public SearchResults search(@PathParam("uid") int uid, SearchQuery query) {
		return null;
	}

}
