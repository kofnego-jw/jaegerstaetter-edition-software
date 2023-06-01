package at.kulinz.jaegerstaetter.frontendmodel.dtomsg;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.ResourceFWDTO;

import java.util.Collections;
import java.util.List;

public class ResourceListMsg extends BasicMsg {

    public final List<ResourceFWDTO> resources;

    public ResourceListMsg(List<ResourceFWDTO> resources) {
        this.resources = resources;
    }

    public ResourceListMsg(String message) {
        super(message);
        this.resources = Collections.emptyList();
    }
}
