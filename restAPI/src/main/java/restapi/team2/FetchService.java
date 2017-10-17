package restapi.team2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import entity.FetchRequest;


@Path("/team2/fetch")
@Consumes(MediaType.APPLICATION_JSON)
public class FetchService {
	
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getFile(FetchRequest fetch) {
		String pathPrefix = "/home/sadm/tmp/";
		
		StreamingOutput fileStream = new StreamingOutput() {
			@Override
			public void write(java.io.OutputStream output) throws IOException, WebApplicationException{
				try {
					java.nio.file.Path path = Paths.get(pathPrefix + fetch.getPath());
					System.out.println(pathPrefix + fetch.getPath());
					byte[] data = Files.readAllBytes(path);
					output.write(data);
					output.flush();
					
				} catch(Exception e) {
					//HANDLE ERROR RESPONSE
					throw new WebApplicationException("File Not Found");
				}
			}
		};
		return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename = " + fetch.getFilename()).build(); 
	}
	
}
