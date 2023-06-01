package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import at.kulinz.jaegerstaetter.metadata.authority.model.Sex;
import at.kulinz.jaegerstaetter.metadata.registry.model.PersonInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RegistryEntryPerson implements Serializable {

    public final String key;
    public final String preferredName;
    public final String surname;
    public final String forename;
    public final List<String> addNames;
    public final List<String> roleNames;
    public final String birthDate;
    public final String birthPlace;
    public final List<String> residences;
    public final String deathDate;
    public final String deathPlace;
    public final Sex sex;
    public final String nationality;
    public final String note;
    public final List<String> controlledVocabularies;
    public final String generatedReadableName;
    public final String generatedOfficialName;

    @JsonCreator
    public RegistryEntryPerson(@JsonProperty("key") String key,
                               @JsonProperty("preferredName") String preferredName,
                               @JsonProperty("surname") String surname,
                               @JsonProperty("forename") String forename,
                               @JsonProperty("addNames") List<String> addNames,
                               @JsonProperty("roleNames") List<String> roleNames,
                               @JsonProperty("birthDate") String birthDate,
                               @JsonProperty("birthPlace") String birthPlace,
                               @JsonProperty("residences") List<String> residences,
                               @JsonProperty("deathDate") String deathDate,
                               @JsonProperty("deathPlace") String deathPlace,
                               @JsonProperty("sex") Sex sex,
                               @JsonProperty("nationality") String nationality,
                               @JsonProperty("note") String note,
                               @JsonProperty("controlledVocabularies") List<String> controlledVocabularies,
                               @JsonProperty("generatedReadableName") String generatedReadableName,
                               @JsonProperty("generatedOfficialName") String generatedOfficialName) {
        this.key = key;
        this.preferredName = preferredName;
        this.surname = surname;
        this.forename = forename;
        this.addNames = addNames == null ? Collections.emptyList() : addNames;
        this.roleNames = roleNames == null ? Collections.emptyList() : roleNames;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.residences = residences == null ? Collections.emptyList() : residences;
        this.deathDate = deathDate;
        this.deathPlace = deathPlace;
        this.sex = sex;
        this.nationality = nationality;
        this.note = note;
        this.controlledVocabularies = controlledVocabularies == null ? Collections.emptyList() : controlledVocabularies;
        this.generatedReadableName = generatedReadableName;
        this.generatedOfficialName = generatedOfficialName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistryEntryPerson that = (RegistryEntryPerson) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    public static RegistryEntryPerson fromPersonInfo(PersonInfo pi) {
        if (pi == null) {
            return null;
        }
        return new RegistryEntryPerson(pi.key, pi.preferredName, pi.surname, pi.forename, pi.addNames,
                pi.roleNames, pi.birthDate, pi.birthPlace, pi.residences, pi.deathDate, pi.deathPlace,
                pi.sex, pi.nationality, pi.note, RegistryHelper.toLinks(pi.controlledVocabularies),
                pi.generatedReadableName, pi.generatedOfficialName);
    }

    public static List<RegistryEntryPerson> fromPersonInfoList(List<PersonInfo> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream().map(RegistryEntryPerson::fromPersonInfo).collect(Collectors.toList());
    }
}
