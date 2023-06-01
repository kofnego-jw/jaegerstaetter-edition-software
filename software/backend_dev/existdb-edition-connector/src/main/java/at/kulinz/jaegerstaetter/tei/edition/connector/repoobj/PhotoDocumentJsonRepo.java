package at.kulinz.jaegerstaetter.tei.edition.connector.repoobj;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.PhotoDocumentGroup;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class PhotoDocumentJsonRepo {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public final List<PhotoDocumentGroup> photoDocumentGroups;

    @JsonCreator
    public PhotoDocumentJsonRepo(@JsonProperty("photoDocumentGroups") List<PhotoDocumentGroup> photoDocumentGroups) {
        this.photoDocumentGroups = photoDocumentGroups;
    }

    public static PhotoDocumentJsonRepo readJson(byte[] bytes) throws Exception {
        return OBJECT_MAPPER.readValue(bytes, PhotoDocumentJsonRepo.class);
    }

    @JsonIgnore
    public byte[] toJson() throws Exception {
        return OBJECT_MAPPER.writeValueAsBytes(this);
    }
}
