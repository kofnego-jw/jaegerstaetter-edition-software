package at.kulinz.jaegerstaetter.xmlservice;

public class XmlServiceException extends Exception {

    public XmlServiceException() {
    }

    public XmlServiceException(String message) {
        super(message);
    }

    public XmlServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlServiceException(Throwable cause) {
        super(cause);
    }
}
