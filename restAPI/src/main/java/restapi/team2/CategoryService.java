package restapi.team2;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import controller.CategoryController;
import entity.Category;
import entity.Condition;


@Path("team2/category")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryService {
	CategoryController cc = new CategoryController();
	
	@GET
	@Path("/list")
	public ArrayList<Category> listCategories(){
		return cc.listAllCategories();
	}
	
	@GET
	@Path("{category_id}")
	public ArrayList<Condition> listConditions(@PathParam("category_id") int category_id){
		return cc.listConditionsFromCategory(category_id);
	}
	
}
