@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(type=LocalDate.class, 
        value=utils.LocalDateAdapter.class),
    @XmlJavaTypeAdapter(type=Timestamp.class, 
    value=utils.TimeStampAdapter.class),
})

package restapi.team1;

import java.sql.Timestamp;
import java.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;