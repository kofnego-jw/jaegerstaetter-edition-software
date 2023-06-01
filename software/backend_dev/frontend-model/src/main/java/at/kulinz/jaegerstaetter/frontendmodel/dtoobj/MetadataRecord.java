package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class MetadataRecord implements Serializable {

    public final String fieldname;

    public final String content;

    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> externalLinks;

    @JsonCreator
    public MetadataRecord(@JsonProperty("fieldname") String fieldname,
                          @JsonProperty("content") String content,
                          @JsonProperty("externalLinks") List<String> externalLinks) {
        this.fieldname = fieldname;
        this.content = content;
        this.externalLinks = externalLinks == null ? Collections.emptyList() : externalLinks;
    }

    @Override
    public String toString() {
        return "MetadataRecord{" +
                "fieldname='" + fieldname + '\'' +
                ", content='" + content + '\'' +
                ", externalLinks=" + externalLinks +
                '}';
    }
}
