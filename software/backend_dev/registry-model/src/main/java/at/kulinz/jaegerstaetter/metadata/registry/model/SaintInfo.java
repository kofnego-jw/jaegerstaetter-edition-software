package at.kulinz.jaegerstaetter.metadata.registry.model;

import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import at.kulinz.jaegerstaetter.metadata.authority.model.Sex;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaintInfo implements AbstractInfo {

    public final String key;
    public final String title;

    public final String preferredName;
    public final String encyclopediaLink;
    public final List<ControlledVocabulary> controlledVocabularies;
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
    public final String todo;
    public String generatedName;


    @JsonCreator
    public SaintInfo(
            @JsonProperty("key") String key,
            @JsonProperty("title") String title,
            @JsonProperty("preferredName") String preferredName,
            @JsonProperty("encyclopediaLink") String encyclopediaLink,
            @JsonProperty("controlledVocabularies") List<ControlledVocabulary> controlledVocabularies,
            @JsonProperty("surname") String surname, @JsonProperty("forename") String forename, @JsonProperty("addNames") List<String> addNames,
            @JsonProperty("rolenames") List<String> rolenames, @JsonProperty("birthDate") String birthDate,
            @JsonProperty("birthPlace") String birthPlace, @JsonProperty("residences") List<String> residences,
            @JsonProperty("deathDate") String deathDate, @JsonProperty("deathPlace") String deathPlace, @JsonProperty("sex") Sex sex,
            @JsonProperty("note") String note,
            @JsonProperty("todo") String todo,
            @JsonProperty("generatedName") String generatedName) {
        this.key = key;
        this.title = title;
        this.preferredName = preferredName;
        this.encyclopediaLink = encyclopediaLink;
        this.controlledVocabularies = controlledVocabularies;
        this.surname = surname;
        this.forename = forename;
        this.addNames = addNames;
        this.rolenames = rolenames;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.residences = residences;
        this.deathDate = deathDate;
        this.deathPlace = deathPlace;
        this.sex = sex;
        this.note = note;
        this.todo = todo;
        this.generatedName = generatedName;
    }

    @JsonIgnore
    public String getReadableName() {
        if (!StringUtils.isBlank(preferredName)) {
            return this.preferredName;
        }
        if (!StringUtils.isBlank(this.title)) {
            return this.title;
        }
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isBlank(this.title)) {
            sb.append(this.title);
        }
        StringBuilder name = new StringBuilder();
        if (!StringUtils.isBlank(this.forename)) {
            name.append(this.forename).append(" ");
        }
        if (!StringUtils.isBlank(this.surname)) {
            name.append(this.surname);
        }
        String nameString = name.toString().trim();
        if (!StringUtils.isBlank(nameString)) {
            sb.append(" (").append(nameString).append(")");
        }
        return sb.toString().trim();
    }

    @Override
    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getPreferredName() {
        if (StringUtils.isBlank(this.preferredName)) {
            return getReadableName();
        }
        return preferredName;
    }

    public String getEncyclopediaLink() {
        return encyclopediaLink;
    }

    public List<ControlledVocabulary> getControlledVocabularies() {
        return controlledVocabularies;
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

    public List<String> getRolenames() {
        return rolenames;
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

    public String getNote() {
        return note;
    }

    public String getTodo() {
        return todo;
    }

    public void clearGeneratedName() {
        this.generatedName = null;
    }

    @Override
    public String toString() {
        return "SaintInfo{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", encyclopediaLink='" + encyclopediaLink + '\'' +
                ", controlledVocabularies=" + controlledVocabularies +
                ", surname='" + surname + '\'' +
                ", forename='" + forename + '\'' +
                ", addNames=" + addNames +
                ", rolenames=" + rolenames +
                ", birthDate='" + birthDate + '\'' +
                ", birthPlace='" + birthPlace + '\'' +
                ", residences=" + residences +
                ", deathDate='" + deathDate + '\'' +
                ", deathPlace='" + deathPlace + '\'' +
                ", sex=" + sex +
                ", note='" + note + '\'' +
                '}';
    }
}
