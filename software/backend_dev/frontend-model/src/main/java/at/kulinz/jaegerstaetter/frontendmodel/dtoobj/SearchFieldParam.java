package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SearchFieldParam implements Serializable {

    public final SearchField field;

    public final String queryString;

    public final SearchOccur occur;

    @JsonCreator
    public SearchFieldParam(@JsonProperty("field") SearchField field,
                            @JsonProperty("queryString") String queryString,
                            @JsonProperty("occur") SearchOccur occur) {
        this.field = field;
        this.queryString = queryString;
        this.occur = occur;
    }
}
