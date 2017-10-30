package restapi.team2;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import controller.EthnicityNationalityController;
import entity.BasicString;

@Path("team2/nationality")
@Produces(MediaType.APPLICATION_JSON)
public class NationalityService {
	
	EthnicityNationalityController enc = new EthnicityNationalityController();
	
	@GET
	public ArrayList<BasicString> getNationality(){
		return enc.getNationality();
	}
	
}
