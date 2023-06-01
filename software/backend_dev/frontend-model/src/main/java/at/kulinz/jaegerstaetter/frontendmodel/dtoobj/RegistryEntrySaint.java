package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import at.kulinz.jaegerstaetter.metadata.authority.model.Sex;
import at.kulinz.jaegerstaetter.metadata.registry.model.SaintInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RegistryEntrySaint implements Serializable {

    public final String key;
    public final String title;
    public final String preferredName;
    public final String encyclopediaLink;
    public final List<String> controlledVocabularies;
    public final String surname;
    public final String forename;
    public final List<String> addNames;
    public final List<String> rolenames;
    public final String birthDate;
    public final String birthPlace;
    public final List<String> residences;
    public final String deathDate;
    public final String deathPlace;
    public final Sex sex;
    public final String note;
    public final String generatedName;

    @JsonCreator
    public RegistryEntrySaint(@JsonProperty("key") String key,
                              @JsonProperty("title") String title,
                              @JsonProperty("preferredName") String preferredName,
                              @JsonProperty("encyclopediaLink") String encyclopediaLink,
                              @JsonProperty("controlledVocabularies") List<String> controlledVocabularies,
                              @JsonProperty("surname") String surname,
                              @JsonProperty("forename") String forename,
                              @JsonProperty("addNames") List<String> addNames,
                              @JsonProperty("rolenames") List<String> rolenames,
                              @JsonProperty("birthDate") String birthDate,
                              @JsonProperty("birthPlace") String birthPlace,
                              @JsonProperty("residences") List<String> residences,
                              @JsonProperty("deathDate") String deathDate,
                              @JsonProperty("deathPlace") String deathPlace,
                              @JsonProperty("sex") Sex sex,
                              @JsonProperty("note") String note,
                              @JsonProperty("generatedName") String generatedName) {
        this.key = key;
        this.title = title;
        this.preferredName = preferredName;
        this.encyclopediaLink = encyclopediaLink;
        this.controlledVocabularies = controlledVocabularies == null ? Collections.emptyList() : controlledVocabularies;
        this.surname = surname;
        this.forename = forename;
        this.addNames = addNames == null ? Collections.emptyList() : addNames;
        this.rolenames = rolenames == null ? Collections.emptyList() : rolenames;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.residences = residences == null ? Collections.emptyList() : residences;
        this.deathDate = deathDate;
        this.deathPlace = deathPlace;
        this.sex = sex;
        this.note = note;
        this.generatedName = generatedName;
    }

    public static RegistryEntrySaint fromSaintInfo(SaintInfo si) {
        if (si == null) {
            return null;
        }
        List<String> controlledVocs = RegistryHelper.toLinks(si.controlledVocabularies);
        return new RegistryEntrySaint(si.key, si.title, si.preferredName, si.encyclopediaLink, controlledVocs, si.surname, si.forename,
                si.addNames, si.rolenames, si.birthDate, si.birthPlace, si.residences, si.deathDate, si.deathPlace, si.sex, si.note, si.generatedName);
    }

    public static List<RegistryEntrySaint> fromSaintInfoList(List<SaintInfo> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream().map(RegistryEntrySaint::fromSaintInfo).collect(Collectors.toList());
    }


}
