package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class SearchHit implements Serializable {

    public final int hitNumber;

    public final String documentId;
    public final ResourceFWDTO resource;
    public final List<SearchHitPreview> previews;

    @JsonCreator
    public SearchHit(@JsonProperty("hitNumber") int hitNumber,
                     @JsonProperty("documentId") String documentId,
                     @JsonProperty("resource") ResourceFWDTO resource,
                     @JsonProperty("previews") List<SearchHitPreview> previews) {
        this.hitNumber = hitNumber;
        this.documentId = documentId;
        this.resource = resource;
        this.previews = previews == null ? Collections.emptyList() : previews;
    }
}
