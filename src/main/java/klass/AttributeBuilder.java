package klass;

public class AttributeBuilder extends AbstractBuilder {
    private Boolean visible;

    public void addDefinition(String definition) {
        parseName(definition);
        parseType(definition);
        parseVisibility(definition);
    }

    private Boolean declaresValue(String definition) {
        return definition.contains("=");
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Boolean isVisible() {
        return visible;
    }

    public Attribute build() {
        return new Attribute(name, type, visible);
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
        visible = null;
    }
}