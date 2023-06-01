package at.kulinz.jaegerstaetter.frontendmodel;

public class FrontendModelException extends Exception {

    public FrontendModelException() {
    }

    public FrontendModelException(String message) {
        super(message);
    }

    public FrontendModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrontendModelException(Throwable cause) {
        super(cause);
    }
}
