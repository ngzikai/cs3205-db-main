package restapi.team2;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import controller.EthnicityNationalityController;
import entity.BasicString;


@Path("team2/ethnicity")
@Produces(MediaType.APPLICATION_JSON)
public class EthnicityService {
	
	EthnicityNationalityController enc = new EthnicityNationalityController();
	
	@GET
	public ArrayList<BasicString> getEthnicity(){
		return enc.getEthnicity();
	}
}
