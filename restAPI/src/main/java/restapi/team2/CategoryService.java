package restapi.team2;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import controller.CategoryController;
import entity.Category;
import entity.CategoryRequest;
import entity.Condition;


@Path("team2/category")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryService {
	CategoryController cc = new CategoryController();
	
	@GET
	@Path("/info")
	public ArrayList<Category> listCategories(){
		return cc.listAllCategories();
	}
	
	@GET
	@Path("/list")
	public ArrayList<Category> listCategoryConditions(){
		return cc.listAllCategoryConditions();
	}
	
	@GET
	@Path("{category_id}")
	public ArrayList<Condition> listConditions(@PathParam("category_id") int category_id){
		return cc.listConditionsFromCategory(category_id);
	}
	
	@POST
	@Path("/request")
	public String requestCategory(CategoryRequest request) {
		return cc.newRequest(request);
	}
	
	@POST
	@Path("/approve")
	public String approveCategory(CategoryRequest request) {
		return cc.approveRequest(request);
	}
	
	@POST
	@Path("/decline")
	public String declineCategory(CategoryRequest request) {
		return cc.declineRequest(request);
	}
	
}
