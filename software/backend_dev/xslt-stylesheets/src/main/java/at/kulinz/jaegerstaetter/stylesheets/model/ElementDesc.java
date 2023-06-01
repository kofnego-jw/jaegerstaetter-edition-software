package at.kulinz.jaegerstaetter.stylesheets.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;
import java.util.stream.Collectors;

public class ElementDesc {

    public final String elementName;

    @JacksonXmlElementWrapper(useWrapping = false)
    public final List<AttributeDesc> attributeDescList;

    @JsonCreator
    public ElementDesc(@JsonProperty("elementName") String elementName, @JsonProperty("attributeDescList") List<AttributeDesc> attributeDescList) {
        this.elementName = elementName;
        this.attributeDescList = attributeDescList;
    }

    public List<String> getAllAttributeNames() {
        return attributeDescList.stream().map(x -> x.attributeName)
                .sorted()
                .collect(Collectors.toList());
    }

    public boolean hasAttribute(String attributeName) {
        return this.getAllAttributeNames().contains(attributeName);
    }

    public boolean hasAttributeAndValue(String attrName, String val) {
        return this.attributeDescList.stream().filter(x -> x.attributeName.equals(attrName))
                .anyMatch(x -> x.hasValue(val));
    }
}
