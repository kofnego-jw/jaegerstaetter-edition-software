package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class PhotoDocumentItem implements Serializable {

    public final String id;
    public final String signature;
    public final String title;
    public final String dating;
    public final String datingNotBefore;
    public final String datingNotAfter;
    public final String place;
    public final String provenience;
    public final String content;
    public final String copyright;
    public final String pageCount;
    public final List<String> jpegs;

    @JsonCreator
    public PhotoDocumentItem(@JsonProperty("id") String id,
                             @JsonProperty("signature") String signature,
                             @JsonProperty("title") String title,
                             @JsonProperty("dating") String dating,
                             @JsonProperty("datingNotBefore") String datingNotBefore,
                             @JsonProperty("datingNotAfter") String datingNotAfter,
                             @JsonProperty("place") String place,
                             @JsonProperty("provenience") String provenience,
                             @JsonProperty("content") String content,
                             @JsonProperty("copyright") String copyright,
                             @JsonProperty("pageCount") String pageCount,
                             @JsonProperty("jpegs") List<String> jpegs) {
        this.id = id;
        this.signature = signature;
        this.title = title;
        this.dating = dating;
        this.datingNotBefore = datingNotBefore;
        this.datingNotAfter = datingNotAfter;
        this.place = place;
        this.provenience = provenience;
        this.content = content;
        this.copyright = copyright;
        this.pageCount = pageCount;
        this.jpegs = jpegs;
    }
}
