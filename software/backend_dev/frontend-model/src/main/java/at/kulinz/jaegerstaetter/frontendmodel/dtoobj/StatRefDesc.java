package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import java.util.List;

public class StatRefDesc {

    public final String destination;

    public final List<String> occurrences;

    public StatRefDesc(String destination, List<String> occurrences) {
        this.destination = destination;
        this.occurrences = occurrences;
    }

    @Override
    public String toString() {
        return "RefDesc{" +
                "destination='" + destination + '\'' +
                ", occurrences=" + occurrences +
                '}';
    }
}
