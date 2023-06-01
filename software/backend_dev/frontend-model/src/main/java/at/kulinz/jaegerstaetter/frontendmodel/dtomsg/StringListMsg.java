package at.kulinz.jaegerstaetter.frontendmodel.dtomsg;

import java.util.Collections;
import java.util.List;

public class StringListMsg extends BasicMsg {

    public final List<String> list;

    public StringListMsg(List<String> list) {
        super();
        this.list = list;
    }

    public StringListMsg(String message) {
        super(message);
        this.list = Collections.emptyList();
    }
}
