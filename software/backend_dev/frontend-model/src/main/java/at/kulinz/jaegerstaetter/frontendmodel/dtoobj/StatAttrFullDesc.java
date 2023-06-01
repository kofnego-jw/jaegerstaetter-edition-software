package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import java.util.List;

public class StatAttrFullDesc {

    public final String attributeName;

    public final List<StatAttrValueOcc> occurrences;

    public StatAttrFullDesc(String attributeName, List<StatAttrValueOcc> occurrences) {
        this.attributeName = attributeName;
        this.occurrences = occurrences;
    }

    @Override
    public String toString() {
        return "AttrFullDesc{" +
                "attributeName='" + attributeName + '\'' +
                ", occurrences=" + occurrences +
                '}';
    }
}
