package at.kulinz.jaegerstaetter.stylesheets.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

public class AttributeDesc {

    public final String attributeName;

    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<String> attributeValues;

    @JsonCreator
    public AttributeDesc(@JsonProperty("attributeName") String attributeName, @JsonProperty("attributeValues") List<String> attributeValues) {
        this.attributeName = attributeName;
        this.attributeValues = attributeValues;
    }

    public boolean hasValue(String val) {
        return this.attributeValues.contains(val);
    }
}
