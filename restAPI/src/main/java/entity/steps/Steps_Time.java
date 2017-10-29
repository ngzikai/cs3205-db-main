package entity.steps;

import java.util.ArrayList;

public class Steps_Time implements java.io.Serializable{
    private static final String MILLISECONDS = "milliseconds";
    private static final String MULTIPLIER = "0.001";

    private String unit = MILLISECONDS;
    private String multiplier = MULTIPLIER;
    private ArrayList<Long> values;

    public String getUnit() {
        return unit;
    }

    public String getMultiplier() {
        return multiplier;
    }

    public ArrayList<Long> getValues() {
        return values;
    }

    public void setValues(ArrayList<Long> values) {
        this.values = values;
    }
}
