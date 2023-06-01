package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import java.util.List;

public class StatAttrValueOcc {

    public final String value;

    public final List<String> occurrences;

    public StatAttrValueOcc(String value, List<String> occurrences) {
        this.value = value;
        this.occurrences = occurrences;
    }

    @Override
    public String toString() {
        return "AttrValueOcc{" +
                "value='" + value + '\'' +
                ", occurrences=" + occurrences +
                '}';
    }
}
