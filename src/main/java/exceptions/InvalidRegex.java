package exceptions;

public class InvalidRegex extends RuntimeException {
    public InvalidRegex(String message) {
        super(message);
    }
}
