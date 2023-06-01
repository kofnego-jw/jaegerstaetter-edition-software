package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IndexEntryBiblePassages {

    public final RegistryEntryBibleBook book;

    public final List<RegistryEntryBiblePosition> entries;

    @JsonCreator
    public IndexEntryBiblePassages(@JsonProperty("book") RegistryEntryBibleBook book,
                                   @JsonProperty("entries") List<RegistryEntryBiblePosition> entries) {
        this.book = book;
        this.entries = entries;
    }
}
