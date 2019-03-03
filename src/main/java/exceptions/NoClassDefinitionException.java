package exceptions;

public class NoClassDefinitionException extends InvalidRegex {
    public NoClassDefinitionException() {
        super("Class definition not found");
    }
}
