package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class ResourceFWDTO implements Serializable {

    public final String id;
    public final String title;
    public final String dating;
    public final String datingReadable;
    public final ResourceType type;
    public final String summary;
    public final List<EditionCorpus> corpora;
    public final List<EditionTimePeriod> periods;
    public final List<String> authors;
    public final List<String> recipients;
    public final List<String> places;
    public final List<String> objectTypes;
    public final String signature;
    public final String altSignature;

    @JsonCreator
    public ResourceFWDTO(@JsonProperty("id") String id,
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
                         @JsonProperty("altSignature") String altSignature) {
        this.id = id;
        this.title = title;
        this.dating = dating;
        this.datingReadable = datingReadable;
        this.type = type;
        this.summary = summary;
        this.corpora = corpora;
        this.periods = periods;
        this.authors = authors;
        this.recipients = recipients;
        this.places = places;
        this.objectTypes = objectTypes;
        this.signature = signature;
        this.altSignature = altSignature;
    }
}
