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
    }

    protected void parseType(String definition) {
        type = definition.split("\\s")[presentModifiers(definition)];
    }

    protected int presentModifiers(String definition) {
        int presentModifiers = 0;
        List<String> possibleModifiers = Arrays.asList("protected", "public", "private", "static", "final", "<.*>");
        List<String> words = Arrays.asList(definition.split("\\s"));

        for (String modifier : possibleModifiers) {
            if (words.stream().anyMatch(word -> word.matches(modifier))) presentModifiers++;
        }

        return presentModifiers;
    }

    public abstract void clear();
}