package restapi.team2;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import controller.ResearcherController;
import entity.Researcher;

@Path("team2/researcher")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResearcherService {
	ResearcherController rc = new ResearcherController();
	
	@GET
	@Path("/login")
	public Researcher Login(Researcher login) {
		return rc.login(login);
	}
	
	@GET
	@Path("/{uid}")
	public Researcher getResearcher(@PathParam("uid") int uid) {
		return null;
	}
	
	@GET
	@Path("/{uid}/category")
	public Researcher getResearcherCategory(@PathParam("uid") int uid) {
		return null;
	}

	@POST
	public void addResearcher(Researcher newResearcher) {
		rc.addResearcher(newResearcher);
	}

	@PUT
	@Path("/{uid}")
	public Researcher updateResearcher(@PathParam("uid") int uid, Researcher researcher) {
		return null;
	}

	@DELETE
	@Path("/{uid}")
	public void deleteResearcher(@PathParam("uid") int uid) {
		rc.deleteResearcher(uid);
	}
}
