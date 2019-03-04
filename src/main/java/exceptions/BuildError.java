package exceptions;

public class BuildError extends RuntimeException {
    public BuildError(String message) {
        super(message);
    }
}
