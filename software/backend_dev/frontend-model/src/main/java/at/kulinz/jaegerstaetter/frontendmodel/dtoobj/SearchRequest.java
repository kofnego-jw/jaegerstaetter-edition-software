package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class SearchRequest implements Serializable {

    public final List<SearchFieldParam> queryParams;
    public final int pageNumber;
    public final int pageSize;
    public final String sortField;
    public final boolean sortAsc;

    @JsonCreator
    public SearchRequest(@JsonProperty("queryParams") List<SearchFieldParam> queryParams,
                         @JsonProperty("pageNumber") int pageNumber,
                         @JsonProperty("pageSize") int pageSize,
                         @JsonProperty("sortField") String sortField,
                         @JsonProperty("sortAsc") boolean sortAsc) {
        this.queryParams = queryParams;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortField = sortField;
        this.sortAsc = sortAsc;
    }
}
