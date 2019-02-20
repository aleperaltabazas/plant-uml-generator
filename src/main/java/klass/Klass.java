package klass;

import java.util.List;
import java.util.Optional;

public class Klass {
    private List<Attribute> attributes;
    private List<Method> methods;
    private String name;
    private ClassType type;
    private Optional<String> parent;

    public Klass(List<Attribute> attributes, List<Method> methods, String name, ClassType type, String parent) {
        this.attributes = attributes;
        this.methods = methods;
        this.name = name;
        this.type = type;
        this.parent = Optional.ofNullable(parent);
    }

    public String getName() {
        return name;
    }

    public ClassType getType() {
        return type;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public String getParent() {
        return parent.orElse("");
    }
}
