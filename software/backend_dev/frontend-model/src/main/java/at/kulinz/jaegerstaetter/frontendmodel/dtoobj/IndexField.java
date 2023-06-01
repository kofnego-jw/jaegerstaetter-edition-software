package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

public class IndexField {
    public final IndexFieldname fieldname;
    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> contents;

    @JsonCreator
    public IndexField(@JsonProperty("fieldname") IndexFieldname fieldname,
                      @JsonProperty("contents") List<String> contents) {
        this.fieldname = fieldname;
        this.contents = contents;
    }

}
