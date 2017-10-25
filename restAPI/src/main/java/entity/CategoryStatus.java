package entity;

public class CategoryStatus {
	private String category_id;
	private String category_name;
	private String status;
	
	public CategoryStatus() {
		
	}

	public CategoryStatus(String category_id, String category_name, String status) {
		super();
		this.category_id = category_id;
		this.category_name = category_name;
		this.status = status;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
