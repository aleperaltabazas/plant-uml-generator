package klass.builders;

import exceptions.BuildError;
import klass.Attribute;

public class AttributeBuilder extends AbstractBuilder {
    public void addDefinition(String definition) {
        parseAnnotations(definition);
        definition = removeAnnotations(definition);
        parseName(definition);
        parseType(definition);
        parseModifiers(definition);
        parseVisibility(definition);
    }

    private boolean declaresValue(String definition) {
        return definition.contains("=");
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

    public Attribute build() throws BuildError {
        if (name == null || type == null) {
            throw new BuildError("Need parameters to build. Name: " + name + ", type: " + type);
        }

        return new Attribute(name, type, visible, modifiers, annotations);
    }

    protected void parseName(String definition) {
        String name = definition.split("\\s")[presentModifiers(definition) + 1];
        if (declaresValue(name)) {
            this.name = name.substring(0, name.indexOf('='));
        } else {
            this.name = name.replace(";", "");
        }
    }

    public void clear() {
        type = null;
        name = null;
    }
}