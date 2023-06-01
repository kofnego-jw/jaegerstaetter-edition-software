package at.kulinz.jaegerstaetter.metadata.authority.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ControlledVocabulary {

    public final Authority authority;
    public final String controlledId;
    public final List<String> titles;
    public final String preferredTitle;

    @JsonCreator
    public ControlledVocabulary(@JsonProperty("authority") Authority authority,
                                @JsonProperty("controlledId") String controlledId,
                                @JsonProperty("titles") List<String> titles,
                                @JsonProperty("preferredTitle") String preferredTitle) {
        this.authority = authority;
        this.controlledId = controlledId;
        this.titles = titles == null ? Collections.emptyList() : titles;
        this.preferredTitle = preferredTitle;
    }

    @JsonIgnore
    public String getAuthorityLink() {
        return this.authority.getBaseUrl() + this.controlledId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ControlledVocabulary that = (ControlledVocabulary) o;
        return authority == that.authority && controlledId.equals(that.controlledId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authority, controlledId);
    }

    @Override
    public String toString() {
        return "ControlledVocabulary{" +
                "authority=" + authority +
                ", controlledId='" + controlledId + '\'' +
                ", titles=" + titles +
                ", preferredTitle='" + preferredTitle + '\'' +
                '}';
    }
}
