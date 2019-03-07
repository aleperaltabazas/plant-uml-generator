package exceptions;

public class NoFkFoundException extends RuntimeException {
    public NoFkFoundException() {
        super("No annotation corresponding to an FK reference was found");
    }
}
