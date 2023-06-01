package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import at.kulinz.jaegerstaetter.metadata.authority.model.GeoLocation;
import at.kulinz.jaegerstaetter.metadata.registry.model.PlaceInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RegistryEntryPlace implements Serializable {


    public final String locationName;

    public final String preferredName;

    public final String region;

    public final String key;

    public final String note;


    public final List<String> controlledVocabularies;

    public final GeoLocation geoLocation;

    public final String generatedName;

    @JsonCreator
    public RegistryEntryPlace(
            @JsonProperty("locationName") String locationName,
            @JsonProperty("preferredName") String preferredName,
            @JsonProperty("region") String region,
            @JsonProperty("key") String key,
            @JsonProperty("note") String note,
            @JsonProperty("controlledVocabularies") List<String> controlledVocabularies,
            @JsonProperty("geoLocation") GeoLocation geoLocation,
            @JsonProperty("generatedName") String generatedName) {
        this.locationName = locationName;
        this.preferredName = preferredName;
        this.region = region;
        this.key = key;
        this.note = note;
        this.controlledVocabularies = controlledVocabularies;
        this.geoLocation = geoLocation;
        this.generatedName = generatedName;
    }

    public static RegistryEntryPlace fromPlaceInfo(PlaceInfo pi) {
        if (pi == null) {
            return null;
        }
        List<String> vocs = RegistryHelper.toLinks(pi.controlledVocabularies);
        return new RegistryEntryPlace(pi.locationName, pi.preferredName, pi.region, pi.key, pi.note, vocs, pi.geoLocation, pi.generatedName);
    }

    public static List<RegistryEntryPlace> fromPlaceInfoList(List<PlaceInfo> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream().map(RegistryEntryPlace::fromPlaceInfo).collect(Collectors.toList());
    }
}
