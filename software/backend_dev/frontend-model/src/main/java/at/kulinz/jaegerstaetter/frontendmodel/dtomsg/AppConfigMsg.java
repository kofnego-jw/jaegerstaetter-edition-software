package at.kulinz.jaegerstaetter.frontendmodel.dtomsg;

public class AppConfigMsg extends BasicMsg {

    public boolean edition;

    public AppConfigMsg(boolean edition) {
        super();
        this.edition = edition;
    }

    public AppConfigMsg(String message) {
        super(message);
        this.edition = true;
    }
}
