package exceptions;

public class NoSuchFileException extends RuntimeException {
    public NoSuchFileException(String path) {
        super("No such file found at " + path);
    }
}
