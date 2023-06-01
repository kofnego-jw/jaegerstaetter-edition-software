package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class SearchHitPreview implements Serializable {

    public final String fieldname;

    public final List<String> snippets;

    @JsonCreator
    public SearchHitPreview(@JsonProperty("fieldname") String fieldname, @JsonProperty("snippets") List<String> snippets) {
        this.fieldname = fieldname;
        this.snippets = snippets == null ? Collections.emptyList() : snippets;
    }
}
