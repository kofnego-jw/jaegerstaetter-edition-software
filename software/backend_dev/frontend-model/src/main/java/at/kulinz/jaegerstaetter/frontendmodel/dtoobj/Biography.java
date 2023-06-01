package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class Biography extends BiographyFW {

    public final String content;

    public final String toc;

    public final List<IndexEntryPerson> index;

    public final String author;

    @JsonCreator
    public Biography(@JsonProperty("persons") List<IndexEntryPerson> persons, @JsonProperty("filename") String filename, @JsonProperty("title") String title, @JsonProperty("content") String content, @JsonProperty("toc") String toc, @JsonProperty("author") String author) {
        super(persons.stream().map(p -> p.entry).collect(Collectors.toList()), filename, title);
        this.index = persons;
        this.content = content;
        this.toc = toc;
        this.author = author;
    }
}
