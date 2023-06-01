package at.kulinz.jaegerstaetter.metadata.registry.model;

import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.GeoLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceInfo implements AbstractInfo {

    public final String locationName;

    public final String preferredName;

    public final String region;

    public final String key;

    public final String note;

    public final String todo;

    public final List<ControlledVocabulary> controlledVocabularies;

    public final GeoLocation geoLocation;

    public String generatedName;

    @JsonCreator
    public PlaceInfo(@JsonProperty("locationName") String locationName,
                     @JsonProperty("region") String region,
                     @JsonProperty("key") String key,
                     @JsonProperty("preferredName") String preferredName,
                     @JsonProperty("note") String note,
                     @JsonProperty("todo") String todo,
                     @JsonProperty("controlledVocabularies") List<ControlledVocabulary> controlledVocabularies,
                     @JsonProperty("geoLocation") GeoLocation geoLocation,
                     @JsonProperty("generatedName") String generatedName) {
        this.locationName = locationName;
        this.region = region;
        this.preferredName = preferredName;
        this.key = key;
        this.note = note;
        this.todo = todo;
        this.controlledVocabularies = controlledVocabularies == null ? Collections.emptyList() : controlledVocabularies;
        this.geoLocation = geoLocation;
        this.generatedName = generatedName;
    }

    public String getLocationName() {
        return locationName;
    }

    @Override
    public String getPreferredName() {
        if (StringUtils.isBlank(preferredName)) {
            return getReadableName();
        }
        return preferredName;
    }

    public String getRegion() {
        return region;
    }

    @Override
    public String getKey() {
        return key;
    }

    public String getNote() {
        return note;
    }

    public String getTodo() {
        return todo;
    }

    public List<ControlledVocabulary> getControlledVocabularies() {
        return controlledVocabularies;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void clearGeneratedName() {
        this.generatedName = null;
    }

    @JsonIgnore
    public String getReadableName() {
        if (!StringUtils.isBlank(this.preferredName)) {
            return this.preferredName;
        }
        if (!StringUtils.isBlank(this.locationName)) {
            return this.locationName;
        }
        return this.key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceInfo placeInfo = (PlaceInfo) o;
        return Objects.equals(key, placeInfo.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "location='" + locationName + '\'' +
                ", region='" + region + '\'' +
                ", key='" + key + '\'' +
                ", note='" + note + '\'' +
                ", controlledVocabularies=" + controlledVocabularies +
                ", geoLocation=" + geoLocation +
                '}';
    }
}
