package at.kulinz.jaegerstaetter.stylesheets.model;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.MetadataGroup;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.ResourceType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.Collections;
import java.util.List;

public class MetadataResult {

    public final String id;
    public final String title;
    public final String dating;
    public final String signature;
    public final String altSignature;
    public final ResourceType type;
    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> facsimileIds;
    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<MetadataGroup> metadataGroups;
    public final String summary;
    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> corpus;
    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> periods;
    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> authors;
    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> recipients;
    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> places;
    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> objectTypes;
    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> prevLetterIds;
    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> nextLetterIds;
    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> relatedLetterIds;


    @JsonCreator
    public MetadataResult(@JsonProperty("id") String id,
                          @JsonProperty("title") String title,
                          @JsonProperty("dating") String dating,
                          @JsonProperty("signature") String signature,
                          @JsonProperty("altSignature") String altSignature,
                          @JsonProperty("type") ResourceType type,
                          @JsonProperty("facsimileIds") List<String> facsimileIds,
                          @JsonProperty("metadataGroups") List<MetadataGroup> metadataGroups,
                          @JsonProperty("summary") String summary,
                          @JsonProperty("corpus") List<String> corpus,
                          @JsonProperty("periods") List<String> periods,
                          @JsonProperty("authors") List<String> authors,
                          @JsonProperty("recipients") List<String> recipients,
                          @JsonProperty("places") List<String> places,
                          @JsonProperty("objectTypes") List<String> objectTypes,
                          @JsonProperty("prevLetterIds") List<String> prevLetterIds,
                          @JsonProperty("nextLetterIds") List<String> nextLetterIds,
                          @JsonProperty("relatedLetterIds") List<String> relatedLetterIds
    ) {
        this.id = id;
        this.title = title;
        this.dating = dating;
        this.signature = signature;
        this.altSignature = altSignature;
        this.type = type;
        this.facsimileIds = facsimileIds;
        this.metadataGroups = metadataGroups;
        this.summary = summary;
        this.corpus = corpus;
        this.periods = periods;
        this.authors = authors;
        this.recipients = recipients;
        this.places = places;
        this.objectTypes = objectTypes;
        this.prevLetterIds = process(prevLetterIds);
        this.nextLetterIds = process(nextLetterIds);
        this.relatedLetterIds = process(relatedLetterIds);
    }

    private static List<String> process(List<String> strings) {
        if (strings == null) {
            return Collections.emptyList();
        }
        // Old way of encoding, can be skipped now.
        /*
        if (strings.size() == 1) {
            String s = strings.get(0);
            if (s.contains(";")) {
                return Stream.of(s.split("\\s*;\\s*")).collect(Collectors.toList());
            }
        }
        */
        return strings;
    }

    @Override
    public String toString() {
        return "MetadataResult{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", dating='" + dating + '\'' +
                ", type=" + type +
                ", facsimileIds=" + facsimileIds +
                ", metadataGroups=" + metadataGroups +
                '}';
    }
}
