package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MenuItem implements Serializable {

    public final String displayName;
    public final String id;

    @JsonCreator
    public MenuItem(@JsonProperty("displayName") String displayName, @JsonProperty("id") String id) {
        this.displayName = displayName;
        this.id = id;
    }
}
