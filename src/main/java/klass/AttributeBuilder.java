package klass;

import java.util.Arrays;
import java.util.List;

public class AttributeBuilder extends AbstractBuilder {
    private String type;
    private String name;
    private boolean visible;

    public void addAttributeDefinition(String definition) {
        parseName(definition);
        parseType(definition);

        visible = definition.substring(0, "protected".length() - 1).contains("public");
    }

    private void parseType(String definition) {
        type = definition.split("\\s")[presentModifiers(definition)];
    }

    private void parseName(String definition) {
        String name = definition.split("\\s")[presentModifiers(definition) + 1];
        if (declaresValue(name)) {
            this.name = name.substring(0, name.indexOf('='));
        } else {
            this.name = name.replace(";", "");
        }
    }

    private int presentModifiers(String definition) {
        int presentModifiers = 0;
        List<String> possibleModifiers = Arrays.asList("protected", "public", "private", "static", "final");

        for (String modifier : possibleModifiers) {
            if (definition.contains(modifier)) presentModifiers++;
        }

        return presentModifiers;
    }

    private boolean declaresValue(String definition) {
        return definition.contains("=");
    }

    public void determineVisibility(String body) {
        visible = visible || body.toLowerCase().contains("public " + type + " get" + name.toLowerCase());
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isVisible() {
        return visible;
    }

    public Attribute build() {
        return new Attribute(name, type, visible);
    }
}