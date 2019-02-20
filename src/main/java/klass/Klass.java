package klass;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Klass {
    private List<Attribute> attributes;
    private List<Method> methods;
    private String name;
    private ClassType type;
    private Optional<Klass> parent;

    public Klass(List<Attribute> attributes, List<Method> methods, String name, ClassType type, Klass parent) {
        this.attributes = attributes;
        this.methods = methods;
        this.name = name;
        this.type = type;
        this.parent = Optional.ofNullable(parent);
    }

    public String getName() {
        return name;
    }

    public Klass getParent() {
        try {
            return parent.get();
        } catch (NoSuchElementException e) {
            return this;
        }
    }
}
