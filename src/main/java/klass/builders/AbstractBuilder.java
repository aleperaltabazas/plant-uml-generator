package klass.builders;

import io.vavr.collection.List;
import klass.Modifier;

import static parsing.RegexConstants.ANNOTATION;

public abstract class AbstractBuilder {
    protected String name;
    protected String type;
    protected boolean visible;
    protected List<Modifier> modifiers = List.empty();
    protected List<String> annotations;

    protected void parseVisibility(String definition) {
        String firstModifier = definition.split("\\s")[0];
        visible = firstModifier.equalsIgnoreCase("public");
    }

    protected void parseType(String definition) {
        type = definition.split("\\s")[presentModifiers(definition)];
    }

    protected void parseAnnotations(String definition) {
        annotations = List.of(definition.split("\\s")).filter(word -> word.matches(ANNOTATION));
    }

    protected int presentModifiers(String definition) {
        int presentModifiers = 0;
        List<String> possibleModifiers = List.of("protected", "public", "private", "static", "final", "<.*>");
        List<String> words = List.of(definition.split("\\s"));

        for (String modifier : possibleModifiers) {
            if (words.exists(word -> word.matches(modifier))) presentModifiers++;
        }

        return presentModifiers;
    }

    protected void parseModifiers(String definition) {
        List<String> words = List.of(definition.split("\\s"));

        switch (words.get(0)) {
            case "public":
                modifiers = modifiers.append(Modifier.PUBLIC);
                break;
            case "private":
                modifiers = modifiers.append(Modifier.PRIVATE);
                break;
            case "protected":
                modifiers = modifiers.append(Modifier.PROTECTED);
                break;
            case "default":
                modifiers = modifiers.append(Modifier.DEFAULT);
                break;
            default:
                modifiers = modifiers.append(Modifier.PACKAGE_PRIVATE);
        }

        for (String word : words) {
            if (word.equals("static")) {
                modifiers = modifiers.append(Modifier.STATIC);
            }
            if (word.equals("final")) {
                modifiers = modifiers.append(Modifier.FINAL);
            }
            if (word.equals("abstract")) {
                modifiers = modifiers.append(Modifier.ABSTRACT);
            }
            if (word.matches("<.*>")) {
                modifiers = modifiers.append(Modifier.GENERIC);
            }
        }
    }

    public void addAnotations(List<String> annotations) {
        if (this.annotations == null) {
            this.annotations = List.empty();
        }

        this.annotations = this.annotations.appendAll(annotations);
    }

    protected String removeAnnotations(String definition) {
        StringBuilder sb = new StringBuilder();

        List.of(definition.split("\\s|\n")).filter(word -> !word.matches(ANNOTATION)).forEach(word -> sb.append(word).append(" "));

        return sb.toString();
    }

    public abstract void clear();
}
