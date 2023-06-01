package at.kulinz.jaegerstaetter.metadata.registry.model;

import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.Sex;
import at.kulinz.jaegerstaetter.metadata.registry.repository.PersonNameHelper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Diese Klasse modelliert die Einträge in Personenverzeichnis.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonInfo implements AbstractInfo {

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

    public final String internalNotes;
    public final List<ControlledVocabulary> controlledVocabularies;

    public String generatedReadableName;

    public String generatedOfficialName;

    @JsonCreator
    public PersonInfo(@JsonProperty("key") String key,
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
                      @JsonProperty("internalNotes") String internalNotes,
                      @JsonProperty("controlledVocabularies") List<ControlledVocabulary> controlledVocabularies,
                      @JsonProperty("generatedReadableName") String generatedReadableName,
                      @JsonProperty("generatedOfficialName") String generatedOfficialName) {
        this.key = key;
        this.preferredName = PersonNameHelper.blankOrTrim(preferredName);
        this.surname = PersonNameHelper.blankOrTrim(surname);
        this.forename = PersonNameHelper.blankOrTrim(forename);
        this.addNames = addNames == null ? Collections.emptyList() :
                addNames.stream().flatMap(x -> Stream.of(x.split("\\s*,\\s*"))).collect(Collectors.toList());
        this.roleNames = roleNames;
        this.birthDate = PersonNameHelper.blankOrTrim(birthDate);
        this.birthPlace = PersonNameHelper.blankOrTrim(birthPlace);
        this.residences = residences;
        this.deathDate = PersonNameHelper.blankOrTrim(deathDate);
        this.deathPlace = PersonNameHelper.blankOrTrim(deathPlace);
        this.sex = sex;
        this.nationality = PersonNameHelper.blankOrTrim(nationality);
        this.note = PersonNameHelper.blankOrTrim(note);
        this.internalNotes = PersonNameHelper.blankOrTrim(internalNotes);
        this.controlledVocabularies = controlledVocabularies == null ? Collections.emptyList() : controlledVocabularies;
        this.generatedReadableName = generatedReadableName;
        this.generatedOfficialName = generatedOfficialName;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getPreferredName() {
        if (StringUtils.isEmpty(preferredName)) {
            return getGeneratedOfficialName();
        }
        return preferredName;
    }

    public String getSurname() {
        return surname;
    }

    public String getForename() {
        return forename;
    }

    public List<String> getAddNames() {
        return addNames;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public List<String> getResidences() {
        return residences;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public Sex getSex() {
        return sex;
    }

    public String getNationality() {
        return nationality;
    }

    public String getNote() {
        return note;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public List<ControlledVocabulary> getControlledVocabularies() {
        return controlledVocabularies;
    }

    public String getGeneratedReadableName() {
        return generatedReadableName;
    }

    public void setGeneratedReadableName(String generatedReadableName) {
        this.generatedReadableName = generatedReadableName;
    }

    public String getGeneratedOfficialName() {
        return generatedOfficialName;
    }

    public void setGeneratedOfficialName(String generatedOfficialName) {
        this.generatedOfficialName = generatedOfficialName;
    }

    @JsonIgnore
    public String getReadableName() {
        if (StringUtils.isBlank(this.generatedReadableName)) {
            StringBuilder sb = new StringBuilder();
            if (!StringUtils.isBlank(this.forename)) {
                sb.append(this.forename).append(" ");
            }
            if (!StringUtils.isBlank(this.surname)) {
                sb.append(this.surname);
            }
            return sb.toString().trim();
        }
        return this.generatedReadableName;
    }

    public void clearGeneratedNames() {
        this.generatedReadableName = null;
        this.generatedOfficialName = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonInfo that = (PersonInfo) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "PersonInfo{" +
                "key='" + key + '\'' +
                ", surname='" + surname + '\'' +
                ", forename='" + forename + '\'' +
                ", addNames=" + addNames +
                ", roleNames=" + roleNames +
                ", birthDate='" + birthDate + '\'' +
                ", birthPlace='" + birthPlace + '\'' +
                ", residences=" + residences +
                ", deathDate='" + deathDate + '\'' +
                ", deathPlace='" + deathPlace + '\'' +
                ", sex=" + sex +
                ", nationality='" + nationality + '\'' +
                ", note='" + note + '\'' +
                ", controlledVocabularies=" + controlledVocabularies +
                '}';
    }
}
