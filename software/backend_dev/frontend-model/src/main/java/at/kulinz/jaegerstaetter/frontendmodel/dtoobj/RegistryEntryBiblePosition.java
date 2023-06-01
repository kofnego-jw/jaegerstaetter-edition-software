package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class RegistryEntryBiblePosition implements Comparable<RegistryEntryBiblePosition>, Serializable {
    public final String book;
    public final String position;
    public final String orderPosition;
    public final List<ResourceFWDTO> resources;

    @JsonCreator
    public RegistryEntryBiblePosition(@JsonProperty("book") String book,
                                      @JsonProperty("position") String position,
                                      @JsonProperty("orderPosition") String orderPosition,
                                      @JsonProperty("resources") List<ResourceFWDTO> resources) {
        this.book = book;
        this.position = position;
        this.orderPosition = orderPosition;
        this.resources = resources;
    }

    @Override
    public int compareTo(RegistryEntryBiblePosition o) {
        if (o == null) {
            return -1;
        }
        return orderPosition.compareTo(o.orderPosition);
    }
}
