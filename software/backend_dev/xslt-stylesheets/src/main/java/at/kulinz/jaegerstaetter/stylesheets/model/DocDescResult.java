package at.kulinz.jaegerstaetter.stylesheets.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DocDescResult {

    public final String filename;

    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<ElementDesc> allElementDescs;

    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<ElementDesc> diplElementDescs;

    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<ElementDesc> normElementDescs;

    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> refTargets;

    public final int rsPersonCount;

    public final int rsPlaceCount;

    public final int rsCorpCount;

    public final int footnoteCount;

    @JsonCreator
    public DocDescResult(@JsonProperty("filename") String filename,
                         @JsonProperty("allElementDescs") List<ElementDesc> allElementDescs,
                         @JsonProperty("diplElementDescs") List<ElementDesc> diplElementDescs,
                         @JsonProperty("normElementDescs") List<ElementDesc> normElementDescs,
                         @JsonProperty("refTargets") List<String> refTargets,
                         @JsonProperty("rsPersonCount") int rsPersonCount,
                         @JsonProperty("rsPlaceCount") int rsPlaceCount,
                         @JsonProperty("rsCorpCount") int rsCorpCount,
                         @JsonProperty("footnoteCount") int footnoteCount
    ) {
        this.filename = filename;
        this.allElementDescs = allElementDescs;
        this.diplElementDescs = diplElementDescs;
        this.normElementDescs = normElementDescs;
        this.refTargets = refTargets != null ? refTargets : Collections.emptyList();
        this.rsPersonCount = rsPersonCount;
        this.rsPlaceCount = rsPlaceCount;
        this.rsCorpCount = rsCorpCount;
        this.footnoteCount = footnoteCount;
    }

    public List<String> getAllElementNames() {
        return this.allElementDescs.stream().map(x -> x.elementName).collect(Collectors.toList());
    }

    public List<String> getAttrNameFromAllElement(String elementName) {
        return this.allElementDescs.stream().filter(x -> x.elementName.equals(elementName)).findAny().map(ElementDesc::getAllAttributeNames).orElse(Collections.emptyList());
    }

    public boolean hasElementAttrValInAll(String elementName, String attrName, String val) {
        return this.allElementDescs.stream()
                .filter(x -> x.elementName.equals(elementName))
                .anyMatch(x -> x.hasAttributeAndValue(attrName, val));
    }

    public List<String> getDiplElementNames() {
        return this.diplElementDescs.stream().map(x -> x.elementName).collect(Collectors.toList());
    }

    public List<String> getAttrNameFromDiplElement(String elementName) {
        return this.diplElementDescs.stream().filter(x -> x.elementName.equals(elementName)).findAny().map(ElementDesc::getAllAttributeNames).orElse(Collections.emptyList());
    }

    public boolean hasElementAttrValInDipl(String elementName, String attrName, String val) {
        return this.diplElementDescs.stream()
                .filter(x -> x.elementName.equals(elementName))
                .anyMatch(x -> x.hasAttributeAndValue(attrName, val));
    }

    public List<String> getNormElementNames() {
        return this.normElementDescs.stream().map(x -> x.elementName).collect(Collectors.toList());
    }

    public List<String> getAttrNameFromNormElement(String elementName) {
        return this.normElementDescs.stream().filter(x -> x.elementName.equals(elementName)).findAny().map(ElementDesc::getAllAttributeNames).orElse(Collections.emptyList());
    }

    public boolean hasElementAttrValInNorm(String elementName, String attrName, String val) {
        return this.normElementDescs.stream()
                .filter(x -> x.elementName.equals(elementName))
                .anyMatch(x -> x.hasAttributeAndValue(attrName, val));
    }

    public boolean hasTarget(String target) {
        return this.refTargets.contains(target);
    }
}
