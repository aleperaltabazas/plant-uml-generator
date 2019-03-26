package klass;

import klass.classtype.ClassType;
import klass.objekt.Objekt;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Klass {
    private List<Attribute> attributes;
    private List<Method> methods;
    private String name;
    private ClassType type;
    private List<String> interfaces;
    private Optional<String> parent;
    private List<Modifier> modifiers;
    private List<String> annotations;
    private Klass superKlass;

    public Klass(List<Attribute> attributes, List<Method> methods, String name, ClassType type,
                 List<String> interfaces, List<Modifier> modifiers, List<String> annotations) {
        this.attributes = attributes;
        this.methods = methods;
        this.name = name;
        this.type = type;
        this.superKlass = Objekt.getInstance();
        this.interfaces = interfaces;
        this.modifiers = modifiers;
        this.annotations = annotations;
    }

    public Klass(List<Attribute> attributes, List<Method> methods, String name, ClassType type, Klass superKlass,
                 List<String> interfaces, List<Modifier> modifiers, List<String> annotations) {
        this.attributes = attributes;
        this.methods = methods;
        this.name = name;
        this.type = type;
        this.superKlass = superKlass;
        this.interfaces = interfaces;
        this.modifiers = modifiers;
        this.annotations = annotations;
    }

    public Klass(List<Attribute> attributes, List<Method> methods, String name, ClassType type,
                 List<String> interfaces, String parent, List<Modifier> modifiers, List<String> annotations) {
        this.attributes = attributes;
        this.methods = methods;
        this.name = name;
        this.type = type;
        this.parent = Optional.ofNullable(parent);
        this.interfaces = interfaces;
        this.modifiers = modifiers;
        this.annotations = annotations;
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
        return isCreationalPattern() || isException() || isSpringStuff();
    }

    private boolean isSpringStuff() {
        String lowerCase = name.toLowerCase();
        return annotations.contains("@Service") || annotations.contains("@Controller") || annotations.contains(
                "@Configuration");
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

    public List<String> getAnnotations() {
        return annotations;
    }

    public boolean isEntity() {
        return annotations.stream().anyMatch(a -> a.matches("@Entity([(].*[)])?"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Klass klass = (Klass) o;
        return Objects.equals(attributes, klass.attributes) &&
                Objects.equals(methods, klass.methods) &&
                Objects.equals(name, klass.name) &&
                Objects.equals(type, klass.type) &&
                Objects.equals(interfaces, klass.interfaces) &&
                Objects.equals(parent, klass.parent) &&
                Objects.equals(modifiers, klass.modifiers) &&
                Objects.equals(annotations, klass.annotations) &&
                Objects.equals(superKlass, klass.superKlass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes, methods, name, type, interfaces, parent, modifiers, annotations, superKlass);
    }
}