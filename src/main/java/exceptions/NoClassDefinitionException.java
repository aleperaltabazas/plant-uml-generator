package exceptions;

public class NoClassDefinitionException extends InvalidRegex {
    public NoClassDefinitionException(String classDefinition) {
        super("Class definition not found: " + classDefinition);
    }
}
