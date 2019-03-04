package exceptions;

public class MultiplePrimaryKeyError extends RuntimeException {
    public MultiplePrimaryKeyError(String message) {
        super(message);
    }
}
