package entity.steps;

import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "Steps")
@XmlAccessorType(XmlAccessType.FIELD)
public class Steps implements Serializable {
    public static final String FIELD_UNIT = "unit";
    public static final String FIELD_MULTIPLIER = "multiplier";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_RECORD = "sessionTime";
    public static final String FIELD_TIME = "time";
    public static final String FIELD_CHANNELS = "channels";
    public static final String FIELD_DISPLAY_UNIT = "displayUnit";
    public static final String FIELD_DATA = "data";
    public static final String FIELD_AXIS_X = "x_axis";
    public static final String FIELD_AXIS_Y = "y_axis";
    public static final String FIELD_VALUE = "value";
    public static final String[] FIELD_CHANNELS_TYPES = {"differenceInTime"};

    private static final String MILLISECONDS = "milliseconds";
    private static final String TYPE = "steps";
    private static final String MULTIPLIER = "0.001";
    private static final String SECONDS = "seconds";

    private String title;
    private String type;
    private long sessionTime;
    private String x_axis;
    private String y_axis;
    private Steps_Time time;
    private Steps_Channels channels;

    public Steps(){
        this.type = TYPE;
        this.x_axis = FIELD_TIME;
        this.y_axis = FIELD_CHANNELS;

        this.time = new Steps_Time();
        this.channels = new Steps_Channels();
    }

    public Steps(long timestamp, String title) {
        this.title = title;
        this.type = TYPE;
        this.sessionTime = timestamp;
        this.x_axis = FIELD_TIME;
        this.y_axis = FIELD_CHANNELS;

        this.time = new Steps_Time();
        this.channels = new Steps_Channels();
    }
    public String getType() {
        return type;
    }

    public long getTimestamp() {
        return sessionTime;
    }

    public void setTimestamp(long timestamp) {
        this.sessionTime = timestamp;
    }

    public Steps_Channels getChannels() {
        return channels;
    }

    public void setChannels(Steps_Channels channels) {
        this.channels = channels;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Steps_Time getTime() {
        return time;
    }

    public void setTime(Steps_Time time) {
        this.time = time;
    }

    public String getX() {
        return x_axis;
    }

    public String getY() {
        return y_axis;
    }


}
