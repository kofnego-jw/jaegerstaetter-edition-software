package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import at.kulinz.jaegerstaetter.metadata.registry.model.CorporationInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RegistryEntryCorporation implements Serializable {


    public final String organisation;
    public final String preferredName;
    public final String key;
    public final String note;
    public final List<String> controlledVocabularies;
    public final String generatedName;

    @JsonCreator
    public RegistryEntryCorporation(
            @JsonProperty("organisation") String organisation, @JsonProperty("preferredName") String preferredName,
            @JsonProperty("key") String key, @JsonProperty("note") String note,
            @JsonProperty("controlledVocabularies") List<String> controlledVocabularies,
            @JsonProperty("generatedName") String generatedName) {
        this.organisation = organisation;
        this.preferredName = preferredName;
        this.key = key;
        this.note = note;
        this.controlledVocabularies = controlledVocabularies;
        this.generatedName = generatedName;
    }

    public static RegistryEntryCorporation fromCorporationInfo(CorporationInfo ci) {
        if (ci == null) {
            return null;
        }
        List<String> vocs = RegistryHelper.toLinks(ci.controlledVocabularies);
        return new RegistryEntryCorporation(ci.organisation, ci.preferredName, ci.key, ci.note, vocs, ci.generatedName);
    }

    public static List<RegistryEntryCorporation> fromCorporationInfoList(List<CorporationInfo> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream().map(RegistryEntryCorporation::fromCorporationInfo).collect(Collectors.toList());
    }
}
