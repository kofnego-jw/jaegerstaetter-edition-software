package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class IndexEntryPerson implements Serializable {

    public final RegistryEntryPerson entry;

    public final List<ResourceFWDTO> resources;

    @JsonCreator
    public IndexEntryPerson(@JsonProperty("entry") RegistryEntryPerson entry, @JsonProperty("resources") List<ResourceFWDTO> resources) {
        this.entry = entry;
        this.resources = resources;
    }
}
