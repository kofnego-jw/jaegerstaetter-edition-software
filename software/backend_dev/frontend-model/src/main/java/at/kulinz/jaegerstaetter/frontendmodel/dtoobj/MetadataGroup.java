package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class MetadataGroup implements Serializable {

    public final String groupKey;

    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<MetadataRecord> records;

    @JsonCreator
    public MetadataGroup(@JsonProperty("groupKey") String groupKey,
                         @JsonProperty("records") List<MetadataRecord> records) {
        this.groupKey = groupKey;
        this.records = records == null ? Collections.emptyList() : records;
    }

    @Override
    public String toString() {
        return "MetadataGroup{" +
                "groupKey='" + groupKey + '\'' +
                ", records=" + records +
                '}';
    }
}
