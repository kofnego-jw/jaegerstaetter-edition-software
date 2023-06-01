package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CommentDoc implements Serializable {

    public final String displayName;
    public final LocalDateTime lastModified;
    public final String content;
    public final String toc;
    public final String author;

    @JsonCreator
    public CommentDoc(@JsonProperty("displayName") String displayName,
                      @JsonProperty("lastModified") LocalDateTime lastModified,
                      @JsonProperty("content") String content,
                      @JsonProperty("toc") String toc,
                      @JsonProperty("author") String author) {
        this.displayName = displayName;
        this.lastModified = lastModified;
        this.content = content;
        this.toc = toc;
        this.author = author;
    }
}
