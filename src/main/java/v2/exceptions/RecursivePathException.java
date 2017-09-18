package v2.exceptions;

public class RecursivePathException extends CustomRuntimeException {
    public RecursivePathException(String message) {
        super(message);
    }

    public RecursivePathException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecursivePathException(Throwable cause) {
        super(cause);
    }

    public RecursivePathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
