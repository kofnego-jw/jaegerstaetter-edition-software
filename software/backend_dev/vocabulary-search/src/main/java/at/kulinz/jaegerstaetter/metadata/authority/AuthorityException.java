package at.kulinz.jaegerstaetter.metadata.authority;

public class AuthorityException extends Exception {

    public AuthorityException() {
    }

    public AuthorityException(String message) {
        super(message);
    }

    public AuthorityException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorityException(Throwable cause) {
        super(cause);
    }
}
