package at.kulinz.jaegerstaetter.bibleregistry.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;

public class JsonBibleIndexEntry {

    public static final Comparator<JsonBibleIndexEntry> COMPARATOR = (p1, p2) -> {
        if (p1 == null || StringUtils.isBlank(p1.orderString)) {
            if (p2 == null || StringUtils.isBlank(p2.orderString)) {
                return 0;
            }
            return 1;
        }
        if (p2 == null || StringUtils.isBlank(p2.orderString)) {
            return -1;
        }
        return p1.orderString.compareTo(p2.orderString);
    };

    public final String book;
    public final String position;
    public final String orderString;
    public final List<String> documentIds;

    @JsonCreator
    public JsonBibleIndexEntry(
            @JsonProperty("book") String book,
            @JsonProperty("position") String position,
            @JsonProperty("orderString") String orderString,
            @JsonProperty("documentIds") List<String> documentIds) {
        this.book = book;
        this.position = position;
        this.orderString = orderString;
        this.documentIds = documentIds;
    }
}
