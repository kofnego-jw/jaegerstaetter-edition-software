package at.kulinz.jaegerstaetter.frontendmodel.dtomsg;

public class BasicMsg {

    public static final String DEFAULT_OKAY_MSG = "okay";

    public final String message;
    public final boolean hasError;

    public BasicMsg(String message, boolean hasError) {
        this.message = message;
        this.hasError = hasError;
    }

    public BasicMsg(String message) {
        this(message, true);
    }

    public BasicMsg() {
        this(DEFAULT_OKAY_MSG, false);
    }
}
