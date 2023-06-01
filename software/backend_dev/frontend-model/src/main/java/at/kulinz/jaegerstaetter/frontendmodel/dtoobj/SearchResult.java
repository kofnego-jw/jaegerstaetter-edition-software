package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class SearchResult implements Serializable {

    public final SearchRequest searchRequest;
    public final int totalHits;
    public final int fromNumber;
    public final int toNumber;
    public final List<SearchHit> hits;

    @JsonCreator
    public SearchResult(@JsonProperty("searchRequest") SearchRequest searchRequest,
                        @JsonProperty("totalHits") int totalHits,
                        @JsonProperty("fromNumber") int fromNumber,
                        @JsonProperty("toNumber") int toNumber,
                        @JsonProperty("hits") List<SearchHit> hits) {
        this.searchRequest = searchRequest;
        this.totalHits = totalHits;
        this.fromNumber = fromNumber;
        this.toNumber = toNumber;
        this.hits = hits == null ? Collections.emptyList() : hits;
    }
}
