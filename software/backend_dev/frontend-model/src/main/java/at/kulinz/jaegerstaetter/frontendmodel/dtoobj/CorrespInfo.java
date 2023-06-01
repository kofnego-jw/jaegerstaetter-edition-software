package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CorrespInfo implements Serializable {

    public final String correspId;

    public final List<DocumentInfo> places = new ArrayList<>();

    @JsonCreator
    public CorrespInfo(@JsonProperty("correspId") String correspId, @JsonProperty("places") List<DocumentInfo> places) {
        this.correspId = correspId;
        if (places != null) {
            this.places.addAll(places.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        }
    }

    public boolean contains(DocumentInfo documentInfo) {
        if (documentInfo == null) {
            return false;
        }
        return this.places.contains(documentInfo);
    }

    public void addDocumentInfo(DocumentInfo docInfo) {
        if (!this.contains(docInfo)) {
            this.places.add(docInfo);
        }
    }

    @Override
    public String toString() {
        return "CorrespInfo{" +
                "correspId='" + correspId + '\'' +
                ", places=" + places +
                '}';
    }
}
