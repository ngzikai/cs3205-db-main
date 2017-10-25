package utils;

import java.sql.Timestamp;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TimeStampAdapter extends XmlAdapter<Long, Timestamp>{
    public Timestamp unmarshal(Long v) throws Exception {
    	System.out.println("un marshalling : " + v);
        return new Timestamp(v.longValue());
    }

    public Long marshal(Timestamp v) throws Exception {
    	System.out.println("Marshalling : " + v);
        return v.getTime();
    }


}
