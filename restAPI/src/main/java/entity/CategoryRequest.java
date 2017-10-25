package entity;

public class CategoryRequest {
	private String researcher_id;
	private String category_id;
	
	public CategoryRequest() {
		
	}

	public CategoryRequest(String researcher_id, String category_id) {
		super();
		this.researcher_id = researcher_id;
		this.category_id = category_id;
	}

	public String getResearcher_id() {
		return researcher_id;
	}

	public void setResearcher_id(String researcher_id) {
		this.researcher_id = researcher_id;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	
	
	

}
