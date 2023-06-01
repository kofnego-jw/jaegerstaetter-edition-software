package at.kulinz.jaegerstaetter.search;

public class SearchException extends Exception {

    public SearchException() {
    }

    public SearchException(String message) {
        super(message);
    }

    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchException(Throwable cause) {
        super(cause);
    }
}
