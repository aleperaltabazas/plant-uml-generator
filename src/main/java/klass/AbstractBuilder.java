package klass;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractBuilder {
    protected String name;
    protected String type;
    protected Boolean visible;

    protected void parseVisibility(String definition) {
        String starting = definition.substring(0, "protected".length());
        visible = starting.contains("protected") || starting.contains("private") || starting.contains("public");
    }

    protected void parseType(String definition) {
        type = definition.split("\\s")[presentModifiers(definition)];
    }

    protected int presentModifiers(String definition) {
        int presentModifiers = 0;
        List<String> possibleModifiers = Arrays.asList("protected", "public", "private", "static", "final");

        for (String modifier : possibleModifiers) {
            if (definition.contains(modifier)) presentModifiers++;
        }

        return presentModifiers;
    }

    public abstract void clear();
}