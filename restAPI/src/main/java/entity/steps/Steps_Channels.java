package entity.steps;
import java.util.ArrayList;

public class Steps_Channels implements java.io.Serializable {
	private static final String MILLISECONDS = "milliseconds";
	private static final String MULTIPLIER = "0.001";
	private static final String SECONDS = "seconds";
	private String unit = MILLISECONDS;
	private String multiplier = MULTIPLIER;
	private String displayUnit = SECONDS;
	private ArrayList<Steps_Channel> data = new ArrayList<>();

	public String getUnit() {
		return unit;
	}

	public String getMultiplier() {
		return multiplier;
	}

	public String getDisplayUnit() {
		return displayUnit;
	}

	public ArrayList<Steps_Channel> getData() {
		return data;
	}

	public void setData(ArrayList<Steps_Channel> channels) {
		this.data = channels;
	}
}
