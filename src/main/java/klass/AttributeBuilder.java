package klass;

public class AttributeBuilder extends AbstractBuilder {
    private String type;
    private String name;
    private boolean visible;

    public void addAttributeDefinition(String definition) {
        String[] words = definition.split("\\s");
        String name;
        String type;

        if (declaresVisibility(definition)) {
            name = words[2];
            type = words[1];
        } else {
            name = words[1];
            type = words[0];
        }

        this.type = type;

        if (declaresValue(name)) {
            this.name = name.substring(0, name.indexOf('=')).replace("\\s", "");
        } else {
            try {
                this.name = name.substring(0, name.indexOf(';')).replace("\\s", "");
            } catch (StringIndexOutOfBoundsException e) {
                this.name = name;
            }
        }

        visible = definition.substring(0, "protected".length() - 1).contains("public");
    }

    private boolean declaresValue(String definition) {
        return definition.contains("=");
    }

    public void determineVisibility(String body) {
        visible = visible || body.contains("public get" + name);
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
}