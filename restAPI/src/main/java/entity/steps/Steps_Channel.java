package entity.steps;
import java.util.ArrayList;

public class Steps_Channel implements java.io.Serializable{
    private String name;
    private ArrayList<Long> values;

    public ArrayList<Long> getValues() {
        return values;
    }

    public void setValues(ArrayList<Long> values) {
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
