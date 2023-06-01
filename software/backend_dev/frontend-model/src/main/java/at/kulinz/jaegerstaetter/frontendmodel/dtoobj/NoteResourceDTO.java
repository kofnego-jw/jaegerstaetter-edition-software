package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class NoteResourceDTO implements Serializable {
    public final String resourceId;
    public final String noteId;
    public final String noteContent;

    @JsonCreator
    public NoteResourceDTO(@JsonProperty("resourceId") String resourceId, @JsonProperty("noteId") String noteId, @JsonProperty("noteContent") String noteContent) {
        this.resourceId = resourceId;
        this.noteId = noteId;
        this.noteContent = noteContent;
    }
}
