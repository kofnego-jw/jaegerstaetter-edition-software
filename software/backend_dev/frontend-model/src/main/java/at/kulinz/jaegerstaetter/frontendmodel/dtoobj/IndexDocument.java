package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

public class IndexDocument {

    public final String documentId;

    public final String resourceId;

    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<IndexField> fields = new ArrayList<>();

    @JsonCreator
    public IndexDocument(@JsonProperty("documentId") String documentId,
                         @JsonProperty("resourceId") String resourceId,
                         @JsonProperty("fields") List<IndexField> fields) {
        this.documentId = documentId;
        this.resourceId = resourceId;
        this.fields.addAll(fields);
    }
}
