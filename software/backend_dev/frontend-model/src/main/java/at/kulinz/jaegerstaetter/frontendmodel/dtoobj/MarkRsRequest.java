package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MarkRsRequest implements Serializable {

    public final String key;

    public final RegistryType type;

    @JsonCreator
    public MarkRsRequest(@JsonProperty("key") String key, @JsonProperty("type") RegistryType type) {
        this.key = key;
        this.type = type;
    }
}
