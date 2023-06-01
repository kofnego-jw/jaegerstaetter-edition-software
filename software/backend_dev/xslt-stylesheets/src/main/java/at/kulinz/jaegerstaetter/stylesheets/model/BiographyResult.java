package at.kulinz.jaegerstaetter.stylesheets.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

public class BiographyResult {

    public final String filename;

    public final String title;

    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> persNameKeys;

    @JsonCreator
    public BiographyResult(@JsonProperty("filename") String filename,
                           @JsonProperty("title") String title,
                           @JsonProperty("persNameKeys") List<String> persNameKeys) {
        this.filename = filename;
        this.title = title;
        this.persNameKeys = persNameKeys;
    }
}
