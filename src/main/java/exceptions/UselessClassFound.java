package exceptions;

public class UselessClassFound extends RuntimeException {
    public UselessClassFound(String klassName) {
        super("Useless class found: " + klassName);
    }
}
