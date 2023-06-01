package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class IndexEntryCorporation implements Serializable {

    public final RegistryEntryCorporation entry;

    public final List<ResourceFWDTO> resources;

    @JsonCreator
    public IndexEntryCorporation(@JsonProperty("entry") RegistryEntryCorporation entry,
                                 @JsonProperty("resources") List<ResourceFWDTO> resources) {
        this.entry = entry;
        this.resources = resources;
    }
}
