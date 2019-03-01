package exceptions;

public class UselessClassFound extends Exception {
    public UselessClassFound(String klassName) {
        super("Useless class found: " + klassName);
    }
}
