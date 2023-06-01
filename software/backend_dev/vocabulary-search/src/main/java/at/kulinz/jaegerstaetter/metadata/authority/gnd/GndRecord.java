package at.kulinz.jaegerstaetter.metadata.authority.gnd;

import at.kulinz.jaegerstaetter.metadata.authority.model.Authority;
import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.GeoLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GndRecord {
    public final String id;
    public final List<String> names;

    public final String preferredName;

    public final GeoLocation geoLocation;

    @JsonCreator
    public GndRecord(@JsonProperty("id") String id,
                     @JsonProperty("names") List<String> names,
                     @JsonProperty("preferredName") String preferredName,
                     @JsonProperty("geoLocation") GeoLocation geoLocation) {
        this.id = id;
        this.names = names;
        this.preferredName = preferredName;
        this.geoLocation = geoLocation;
    }

    public ControlledVocabulary toControlledVocabulary() {
        return new ControlledVocabulary(Authority.GND, this.id, this.names, this.preferredName);
    }

    @Override
    public String toString() {
        return "GndRecord{" +
                "id='" + id + '\'' +
                ", names=" + names +
                ", preferredName='" + preferredName + '\'' +
                ", geoLocation=" + geoLocation +
                '}';
    }
}
