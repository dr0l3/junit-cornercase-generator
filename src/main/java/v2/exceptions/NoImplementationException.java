package v2.exceptions;

public class NoImplementationException extends CustomRuntimeException {
    public NoImplementationException(String message) {
        super(message);
    }

    public NoImplementationException(String message, Throwable cause) {
        super(message, cause);
    }
}
