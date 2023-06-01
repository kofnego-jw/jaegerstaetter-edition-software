package at.kulinz.jaegerstaetter.metadata.registry.model;

import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CorporationInfo implements AbstractInfo {

    public final String organisation;
    public final String preferredName;
    public final String key;
    public final String note;
    public final String todo;
    public final List<ControlledVocabulary> controlledVocabularies;
    public String generatedName;

    @JsonCreator
    public CorporationInfo(@JsonProperty("organisation") String organisation,
                           @JsonProperty("key") String key,
                           @JsonProperty("preferredName") String preferredName,
                           @JsonProperty("note") String note,
                           @JsonProperty("todo") String todo,
                           @JsonProperty("controlledVocabularies") List<ControlledVocabulary> controlledVocabularies,
                           @JsonProperty("generatedName") String generatedName) {
        this.organisation = organisation;
        this.key = key;
        this.note = note;
        this.todo = todo;
        this.preferredName = preferredName;
        this.controlledVocabularies = controlledVocabularies;
        this.generatedName = generatedName;
    }

    public String getOrganisation() {
        return organisation;
    }

    @Override
    public String getPreferredName() {
        if (StringUtils.isBlank(preferredName)) {
            return getReadableName();
        }
        return preferredName;
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

    public void clearGeneratedName() {
        this.generatedName = null;
    }

    @JsonIgnore
    public String getReadableName() {
        if (!StringUtils.isBlank(this.preferredName)) {
            return preferredName;
        }
        if (!StringUtils.isBlank(this.organisation)) {
            return organisation;
        }
        return key;
    }

    @Override
    public String toString() {
        return "CorporationInfo{" +
                "organisation='" + organisation + '\'' +
                ", key='" + key + '\'' +
                ", note='" + note + '\'' +
                ", controlledVocabularies=" + controlledVocabularies +
                '}';
    }
}
