package klass;

import exceptions.NoSuchStrategyException;
import io.vavr.collection.List;
import klass.classtype.ClassType;
import klass.objekt.ObjectClass;

import java.util.Objects;

public class Klass {
    private List<Attribute> attributes;
    private List<Method> methods;
    private String name;
    private ClassType type;
    private List<String> interfaces;
    private List<Modifier> modifiers;
    private List<String> annotations;
    private Klass superKlass;

    public Klass(List<Attribute> attributes, List<Method> methods, String name, ClassType type,
                 List<String> interfaces, List<Modifier> modifiers, List<String> annotations) {
        this(attributes, methods, name, type, ObjectClass.getInstance(), interfaces, modifiers, annotations);
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

    public boolean hasGetterFor(String name) {
        return methods.exists(method -> method.getName().equalsIgnoreCase("get" + name));
    }

    public boolean isIgnorable() {
        return isCreationalPattern() || isException() || isSpringStuff();
    }

    private boolean isSpringStuff() {
        return annotations.contains("@Service") || annotations.contains("@Controller") || annotations.contains(
                "@Configuration");
    }

    private boolean isCreationalPattern() {
        String lowerCaseName = name.toLowerCase();

        return lowerCaseName.contains("builder") || lowerCaseName.contains("factory");
    }

    private boolean isException() {
        String lowerCase = this.name.toLowerCase();
        return lowerCase.contains("exception") || lowerCase.contains("throwable") || lowerCase.contains("error") || superKlass.isIgnorable();
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public boolean isEntity() {
        return annotations.exists(a -> a.matches("@Entity([(].*[)])?"));
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
                Objects.equals(modifiers, klass.modifiers) &&
                Objects.equals(annotations, klass.annotations) &&
                Objects.equals(superKlass, klass.superKlass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes, methods, name, type, interfaces, modifiers, annotations, superKlass);
    }

    public Klass getSuperKlass() {
        return superKlass;
    }

    public boolean hasParent() {
        return superKlass != ObjectClass.getInstance();
    }

    public boolean inherits(Method method) {
        return inheritedMethods().contains(method);
    }

    public boolean inherits(Attribute attribute) {
        return inheritedAttributes().contains(attribute);
    }

    protected List<Attribute> inheritedAttributes() {
        return superKlass.getInheritableAttributes();
    }

    protected List<Attribute> getInheritableAttributes() {
        return attributes.filter(attribute -> attribute.isInheritable()).appendAll(superKlass.getInheritableAttributes());
    }

    protected List<Method> inheritedMethods() {
        return superKlass.getInheritableMethods();
    }

    protected List<Method> getInheritableMethods() {
        return methods.filter(method -> method.isInheritable()).appendAll(superKlass.getInheritableMethods());
    }

    public List<Method> allMethods() {
        return methods.appendAll(inheritedMethods());
    }

    public List<Attribute> allAtributes() {
        return attributes.appendAll(inheritedAttributes());
    }

    public boolean isInherited() {
        return ObjectClass.getInstance() != superKlass;
    }

    public boolean inheritanceStrategy() {
        return hasAnnotationByRegex("@Inheritance");
    }

    public boolean mappedSuperclass() {
        return hasAnnotationByRegex("@MappedSuperclass");
    }

    private boolean hasAnnotationByRegex(String regex) {
        return annotations.exists(a -> a.matches(regex + "([(].*[)])?"));
    }

    public String getInheritanceStrategy() {
        return annotations.filter(a -> a.matches("@Inheritance([(].*[)])?"))
                .headOption().getOrElseThrow(() -> new NoSuchStrategyException(this));
    }
}