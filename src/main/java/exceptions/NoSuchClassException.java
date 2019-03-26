package exceptions;

public class NoSuchClassException extends RuntimeException {
    public NoSuchClassException(String superClass) {
        super(superClass);
    }
}
