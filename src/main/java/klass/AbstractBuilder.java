package klass;

public class AbstractBuilder {
    protected boolean declaresVisibility(String definition) {
        String starting = definition.substring(0, "protected".length());
        return starting.contains("protected") || starting.contains("private") || starting.contains("public");
    }
}
