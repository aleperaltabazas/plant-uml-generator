package klass;

import java.util.List;
import java.util.Optional;

public class Klass {
    private List<Attribute> attributes;
    private List<Method> methods;
    private String name;
    private ClassType type;
    private List<String> interfaces;
    private Optional<String> parent;

    public Klass(List<Attribute> attributes, List<Method> methods, String name, ClassType type, List<String> interfaces, String parent) {
        this.attributes = attributes;
        this.methods = methods;
        this.name = name;
        this.type = type;
        this.parent = Optional.ofNullable(parent);
        this.interfaces = interfaces;
    }

    public String getName() {
        return name;
    }

    public ClassType getClassType() {
        return type;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public Optional<String> getParent() {
        return parent;
    }

    public ClassType getType() {
        return type;
    }

    public boolean hasGetterFor(String name) {
        return methods.stream().anyMatch(method -> method.getName().equalsIgnoreCase("get" + name));
    }
}