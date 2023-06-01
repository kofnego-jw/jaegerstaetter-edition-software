package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class ResourceDTO extends ResourceFWDTO {

    public final List<String> facsimileIds;
    public final List<MetadataGroup> metadata;
    public final List<VersionInfo> versions;
    public final String normalizedRepresentation;
    public final String diplomaticRepresentation;
    public final List<ResourceFWDTO> prevLetters;
    public final List<ResourceFWDTO> nextLetters;
    public final List<ResourceFWDTO> relatedLetters;
    public final TocList tocList;

    @JsonCreator
    public ResourceDTO(@JsonProperty("id") String id,
                       @JsonProperty("title") String title,
                       @JsonProperty("dating") String dating,
                       @JsonProperty("datingReadable") String datingReadable,
                       @JsonProperty("type") ResourceType type,
                       @JsonProperty("summary") String summary,
                       @JsonProperty("corpora") List<EditionCorpus> corpora,
                       @JsonProperty("periods") List<EditionTimePeriod> periods,
                       @JsonProperty("authors") List<String> authors,
                       @JsonProperty("recipients") List<String> recipients,
                       @JsonProperty("places") List<String> places,
                       @JsonProperty("objectTypes") List<String> objectTypes,
                       @JsonProperty("signature") String signature,
                       @JsonProperty("altSignature") String altSignature,
                       @JsonProperty("facsimileIds") List<String> facsimileIds,
                       @JsonProperty("metadata") List<MetadataGroup> metadata,
                       @JsonProperty("versions") List<VersionInfo> versions,
                       @JsonProperty("normalizedRepresentation") String normalizedRepresentation,
                       @JsonProperty("diplomaticRepresentation") String diplomaticRepresentation,
                       @JsonProperty("prevLetters") List<ResourceFWDTO> prevLetters,
                       @JsonProperty("nextLetters") List<ResourceFWDTO> nextLetters,
                       @JsonProperty("relatedLetters") List<ResourceFWDTO> relatedLetters,
                       @JsonProperty("tocList") TocList tocList) {
        super(id, title, dating, datingReadable, type, summary, corpora, periods, authors, recipients, places, objectTypes, signature, altSignature);
        this.facsimileIds = facsimileIds == null ? Collections.emptyList() : facsimileIds;
        this.metadata = metadata == null ? Collections.emptyList() : metadata;
        this.versions = versions;
        this.normalizedRepresentation = normalizedRepresentation;
        this.diplomaticRepresentation = diplomaticRepresentation;
        this.prevLetters = prevLetters;
        this.nextLetters = nextLetters;
        this.relatedLetters = relatedLetters;
        this.tocList = tocList;
    }

}
