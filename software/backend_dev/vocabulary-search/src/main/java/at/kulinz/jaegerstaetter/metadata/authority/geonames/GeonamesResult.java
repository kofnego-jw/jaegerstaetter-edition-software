package at.kulinz.jaegerstaetter.metadata.authority.geonames;

import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.GeoLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GeonamesResult {

    public final ControlledVocabulary controlledVocabulary;

    public final GeoLocation geoLocation;

    @JsonCreator
    public GeonamesResult(@JsonProperty("controlledVocabulary") ControlledVocabulary controlledVocabulary,
                          @JsonProperty("geoLocation") GeoLocation geoLocation) {
        this.controlledVocabulary = controlledVocabulary;
        this.geoLocation = geoLocation;
    }

    public ControlledVocabulary toControlledVocabulary() {
        return this.controlledVocabulary;
    }

    @Override
    public String toString() {
        return "GeonamesResult{" +
                "controlledVocabulary=" + controlledVocabulary +
                ", geoLocation=" + geoLocation +
                '}';
    }
}
