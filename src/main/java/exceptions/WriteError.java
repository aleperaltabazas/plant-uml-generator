package exceptions;

public class WriteError extends RuntimeException {
    public WriteError(String s) {
        super(s);
    }
}
