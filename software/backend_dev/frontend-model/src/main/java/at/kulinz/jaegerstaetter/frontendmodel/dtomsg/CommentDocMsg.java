package at.kulinz.jaegerstaetter.frontendmodel.dtomsg;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.CommentDoc;

public class CommentDocMsg extends BasicMsg {

    public final CommentDoc commentDoc;

    public CommentDocMsg(CommentDoc commentDoc) {
        this.commentDoc = commentDoc;
    }

    public CommentDocMsg(String message) {
        super(message);
        this.commentDoc = null;
    }
}
