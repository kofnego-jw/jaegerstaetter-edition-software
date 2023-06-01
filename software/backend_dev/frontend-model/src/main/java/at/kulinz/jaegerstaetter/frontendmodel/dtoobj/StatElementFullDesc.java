package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import java.util.List;

public class StatElementFullDesc {

    public final String elementName;

    public final List<StatAttrFullDesc> attributes;

    public StatElementFullDesc(String elementName, List<StatAttrFullDesc> attributes) {
        this.elementName = elementName;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "ElementFullDesc{" +
                "elementName='" + elementName + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
