package exceptions;

public class MultiplePrimaryKeyError extends RuntimeException {
    public MultiplePrimaryKeyError(String s) {
        super(s);
    }
}