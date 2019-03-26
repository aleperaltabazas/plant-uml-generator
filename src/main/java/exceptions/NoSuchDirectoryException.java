package exceptions;

public class NoSuchDirectoryException extends RuntimeException {
    public NoSuchDirectoryException(String s) {
        super(s);
    }
}
