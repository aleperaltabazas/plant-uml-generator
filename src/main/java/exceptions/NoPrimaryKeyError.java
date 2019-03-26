package exceptions;

public class NoPrimaryKeyError extends RuntimeException {
    public NoPrimaryKeyError(String s) {
        super(s);
    }
}
