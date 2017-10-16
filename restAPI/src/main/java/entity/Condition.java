package entity;

public class Condition {
	private int condition_id;
	private String condition_name;
	
	public Condition() {
		
	}

	public Condition(int condition_id, String condition_name) {
		super();
		this.condition_id = condition_id;
		this.condition_name = condition_name;
	}

	public int getCondition_id() {
		return condition_id;
	}

	public void setCondition_id(int condition_id) {
		this.condition_id = condition_id;
	}

	public String getCondition_name() {
		return condition_name;
	}

	public void setCondition_name(String condition_name) {
		this.condition_name = condition_name;
	}

}
