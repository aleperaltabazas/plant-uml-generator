package klass;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractBuilder {
    protected String name;
    protected String type;
    protected boolean visible;

    protected void parseVisibility(String definition) {
        String firstModifier = definition.split("\\s")[0];
        visible = firstModifier.equalsIgnoreCase("public");
        System.out.println(firstModifier);
        System.out.println(visible);
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