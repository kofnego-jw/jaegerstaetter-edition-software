package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class TocEntry implements Serializable {

    public final String id;
    public final String title;
    public final List<TocEntry> children;

    @JsonCreator
    public TocEntry(@JsonProperty("id") String id,
                    @JsonProperty("title") String title,
                    @JsonProperty("children") List<TocEntry> children) {
        this.id = id;
        this.title = title;
        this.children = children == null ? Collections.emptyList() : children;
    }

    @Override
    public String toString() {
        return "TocEntry{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", children=" + children +
                '}';
    }
}
