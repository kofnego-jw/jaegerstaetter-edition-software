package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class PhotoDocumentGroup implements Serializable {

    public final PhotoDocumentGroupType type;

    public final String groupKey;

    public final String groupTitle;

    public final List<PhotoDocumentItem> items;

    @JsonCreator
    public PhotoDocumentGroup(@JsonProperty("type") PhotoDocumentGroupType type,
                              @JsonProperty("groupKey") String groupKey,
                              @JsonProperty("groupTitle") String groupTitle,
                              @JsonProperty("items") List<PhotoDocumentItem> items) {
        this.type = type == null ? PhotoDocumentGroupType.PHOTO : type;
        this.groupKey = groupKey;
        this.groupTitle = groupTitle;
        this.items = items;
    }
}
