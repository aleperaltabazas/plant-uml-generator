package klass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractBuilder {
    protected String name;
    protected String type;
    protected boolean visible;
    protected List<Modifier> modifiers = new ArrayList<>();

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

    protected void parseModifiers(String definition) {
        List<String> words = Arrays.asList(definition.split("\\s"));

        switch (words.get(0)) {
            case "public":
                modifiers.add(Modifier.Public);
            case "private":
                modifiers.add(Modifier.Private);
            case "protected":
                modifiers.add(Modifier.Protected);
            case "default":
                modifiers.add(Modifier.Default);
            default:
                modifiers.add(Modifier.PackagePrivate);
        }

        for (String word : words) {
            if (word.equals("static"))
                modifiers.add(Modifier.Static);
            if (word.equals("final"))
                modifiers.add(Modifier.Final);
            if (word.equals("abstract"))
                modifiers.add(Modifier.Abstract);
            if (word.matches("<.*>"))
                modifiers.add(Modifier.Generic);
        }
    }

    public abstract void clear();
}