package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class DocumentInfo implements Serializable {

    public final String filename;

    public final String anchorName;

    @JsonCreator
    public DocumentInfo(@JsonProperty("filename") String filename, @JsonProperty("anchorName") String anchorName) {
        this.filename = filename;
        this.anchorName = anchorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentInfo that = (DocumentInfo) o;
        return Objects.equals(filename, that.filename) && Objects.equals(anchorName, that.anchorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, anchorName);
    }

    @Override
    public String toString() {
        return "DocumentInfo{" +
                "filename='" + filename + '\'' +
                ", anchorName='" + anchorName + '\'' +
                '}';
    }
}
