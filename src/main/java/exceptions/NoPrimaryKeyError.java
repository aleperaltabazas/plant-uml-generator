package exceptions;

public class NoPrimaryKeyError extends RuntimeException {
    public NoPrimaryKeyError(String klassName) {
        super("No Primary Key was found for class " + klassName);
    }
}
