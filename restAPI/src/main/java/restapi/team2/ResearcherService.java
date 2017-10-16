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
	@Path("/{researcher_username}")
	public Researcher getDetails(@PathParam("researcher_username") String username) {
		return rc.getResearcher(username);
	}
	
	@POST
	@Path("/login")
	public String Login(Researcher login) {
		//returns hash if available, else false
		return rc.login(login.getResearcher_username());
	}

	@POST
	@Path("/register")
	public String addResearcher(Researcher newResearcher) {
		if(rc.addResearcher(newResearcher)) {
			return "Success";
		}else {
			return "Failed";
		}
	}

	@PUT
	@Path("/edit")
	public String updateResearcher(Researcher researcher) {
		if(rc.updateResearcher(researcher)) {
			return "Success";
		}else {
			return "Failed";
		}
		
	}

	@DELETE
	@Path("/{uid}")
	public String deleteResearcher(@PathParam("uid") int uid) {
		if(rc.deleteResearcher(uid)) {
			return "Success";
		}else {
			return "Failed";
		}
	}
}
