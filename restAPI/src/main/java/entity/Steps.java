package entity;

import java.util.ArrayList;

public class Steps {
    public static final String FIELD_UNIT = "unit";
    public static final String FIELD_MULTIPLIER = "multiplier";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_RECORD = "sessionTime";
    public static final String FIELD_TIME = "time";
    public static final String FIELD_CHANNEL = "channels";
    public static final String FIELD_AXIS_X = "x-axis";
    public static final String FIELD_AXIS_Y = "y-axis";
    public static final String FIELD_VALUE = "value";
    public static final String FIELD_DEFAULT_UNIT = "seconds";
    public static final String FIELD_DISPLAY_UNITS = "displayUnit";
    public static final String[] FIELD_CHANNELS_TYPES = { "noOfSteps", "differenceInTime" };

    private static final String MILLISECONDS = "milliseconds";
    private static final String TYPE = "steps";
    private static final String MULTIPLIER = "0.001";

    private String name;
    private String type;
    private long sessionTime;
    private String x_axis;
    private String y_axis;
    private Time time;
    private ArrayList<Channel> channels;
    private String displayUnit;

    public Steps(long timestamp, String name) {
        this.type = TYPE;
        this.name = name;
        this.sessionTime = timestamp;
        this.x_axis = FIELD_TIME;
        this.y_axis = FIELD_CHANNEL;
        this.displayUnit = FIELD_DEFAULT_UNIT;
    }

    public String getDisplayUnit(){
      return displayUnit;
    }

    public void setDisplayUnit(String displayUnit){
      this.displayUnit = displayUnit;
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

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getX() {
        return x_axis;
    }

    public String getY() {
        return y_axis;
    }

    public class Time {
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

    public class Channel {
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
}
