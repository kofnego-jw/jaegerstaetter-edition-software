package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class IndexEntrySaint implements Serializable {

    public final RegistryEntrySaint entry;

    public final List<ResourceFWDTO> resources;

    @JsonCreator
    public IndexEntrySaint(@JsonProperty("entry") RegistryEntrySaint entry, @JsonProperty("resources") List<ResourceFWDTO> resources) {
        this.entry = entry;
        this.resources = resources;
    }
}
