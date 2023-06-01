package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class IndexEntryPlace implements Serializable {

    public final RegistryEntryPlace entry;

    public final List<ResourceFWDTO> resources;

    @JsonCreator
    public IndexEntryPlace(@JsonProperty("entry") RegistryEntryPlace entry, @JsonProperty("resources") List<ResourceFWDTO> resources) {
        this.entry = entry;
        this.resources = resources;
    }
}
