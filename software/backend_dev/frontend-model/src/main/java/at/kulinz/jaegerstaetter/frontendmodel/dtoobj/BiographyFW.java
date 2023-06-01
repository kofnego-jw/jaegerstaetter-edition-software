package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class BiographyFW implements Serializable {

    public final List<RegistryEntryPerson> persons;
    public final String filename;
    public final String title;

    @JsonCreator
    public BiographyFW(@JsonProperty("persons") List<RegistryEntryPerson> persons, @JsonProperty("filename") String filename, @JsonProperty("title") String title) {
        this.persons = persons;
        this.filename = filename;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiographyFW that = (BiographyFW) o;
        return Objects.equals(filename, that.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename);
    }

    @Override
    public String toString() {
        return "BiographyFW{" +
                "persons=" + persons +
                ", filename='" + filename + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
