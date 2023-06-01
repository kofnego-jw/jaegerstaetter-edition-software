package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class TocList implements Serializable {
    public final List<TocEntry> toc;

    @JsonCreator
    public TocList(@JsonProperty("toc") List<TocEntry> toc) {
        this.toc = toc == null ? Collections.emptyList() : toc;
    }

    @Override
    public String toString() {
        return "TocList{" +
                "toc=" + toc +
                '}';
    }
}
