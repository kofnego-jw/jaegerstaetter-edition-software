package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RegistryEntryBibleBook implements Comparable<RegistryEntryBibleBook>, Serializable {

    public final String book;

    public final String position;

    public final String preferredName;

    public final int counter;

    @JsonCreator
    public RegistryEntryBibleBook(@JsonProperty("book") String book,
                                  @JsonProperty("position") String position,
                                  @JsonProperty("preferredName") String preferredName,
                                  @JsonProperty("counter") int counter) {
        this.book = book;
        this.position = position;
        this.preferredName = preferredName;
        this.counter = counter;
    }

    @Override
    public int compareTo(RegistryEntryBibleBook o) {
        if (o == null) {
            return -1;
        }
        return position.compareTo(o.position);
    }
}
