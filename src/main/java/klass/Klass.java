package klass;

import klass.classtype.ClassType;

import java.util.List;
import java.util.Optional;

public class Klass {
    private List<Attribute> attributes;
    private List<Method> methods;
    private String name;
    private ClassType type;
    private List<String> interfaces;
    private Optional<String> parent;
    private List<Modifier> modifiers;
    private List<String> annotaions;

    public Klass(List<Attribute> attributes, List<Method> methods, String name, ClassType type,
                 List<String> interfaces, String parent, List<Modifier> modifiers, List<String> annotaions) {
        this.attributes = attributes;
        this.methods = methods;
        this.name = name;
        this.type = type;
        this.parent = Optional.ofNullable(parent);
        this.interfaces = interfaces;
        this.modifiers = modifiers;
        this.annotaions = annotaions;
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

    public boolean hasGetterFor(String name) {
        return methods.stream().anyMatch(method -> method.getName().equalsIgnoreCase("get" + name));
    }

    public boolean isIgnorable() {
        return isCreationalPattern() || isException();
    }

    private boolean isCreationalPattern() {
        String lowerCaseName = name.toLowerCase();

        return lowerCaseName.contains("builder") || lowerCaseName.contains("factory");
    }

    private boolean isException() {
        if (parent.isPresent()) {
            String parent = this.parent.get().toLowerCase();
            return parent.contains("exception") || parent.contains("runtimeexception") || parent.contains("throwable");
        }

        return false;
    }

    public List<String> getAnnotaions() {
        return annotaions;
    }

    public boolean isEntity() {
        return annotaions.stream().anyMatch(a -> a.matches("@Entity([(].*[)])?"));
    }
}