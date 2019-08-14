package klass;

import io.vavr.collection.List;

import java.util.Objects;

public class Attribute {
    private String name;
    private String klass;
    private boolean visible;
    private List<Modifier> modifiers;
    private List<String> annotations;

    public Attribute(String name, String klass, boolean visible, List<Modifier> modifiers, List<String> annotations) {
        this.name = name;
        this.klass = klass;
        this.visible = visible;
        this.modifiers = modifiers;
        this.annotations = annotations;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getKlass() {
        return klass;
    }

    public String getName() {
        return name;
    }

    public boolean isIgnored() {
        return isIgnored(klass);
    }

    private boolean isIgnored(String type) {
        return isPrimitive(type) || isSimpleGeneric(type) || isLibrary(type);
    }

    private boolean isLibrary(String type) {
        List<String> standardLibraries = List.of("LocalDate", "LocalDateTime", "Date");

        return standardLibraries.exists(library -> type.matches(library));
    }

    private boolean isSimpleGeneric(String type) {
        if (oneGeneric().exists(g -> type.matches(g))) {
            String generic = type.substring(type.indexOf('<') + 1, type.lastIndexOf('>'));

            return isIgnored(generic);
        } else if (twoGeneric().exists(g -> type.matches(g))) {
            String generic = type.substring(type.lastIndexOf(',') + 1, type.lastIndexOf('>')).replaceAll("\\+s", "");

            return isIgnored(generic);
        }

        return false;
    }

    private List<String> oneGeneric() {
        return List.of("List<.+>" /* listRegex */,
                "Set<.+>" /* setRegex */,
                "Optional<.*>" /*optionalRegex*/);
    }

    private List<String> twoGeneric() {
        return List.of("Map<.+\\s?,\\s?.+>" /* mapRegex */,
                "Pair<.*,.*>" /* pairRegex */);
    }

    private List<String> primitives() {
        List<String> primitives = List.of("int", "float", "double", "char", "short", "long", "boolean", "byte");
        List<String> primitivesAsObjects = List.of("Integer", "Float", "Double", "Character", "Short", "Long",
                "Boolean", "Byte", "String");

        return primitives.appendAll(primitivesAsObjects);
    }

    private boolean isPrimitive(String type) {
        return primitives().contains(type);
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public boolean isPrimaryKey() {
        return hasAnnotation("@Id");
    }

    public boolean isForeignKey() {
        return hasAnnotation("(@ManyToOne([(].*[)])?|@OneToOne([(].*[)])?|@OneToMany([(].*[)])?|@ManyToMany([(].*[)])" +
                "?)");
    }

    public boolean isTransient() {
        return hasAnnotation("@Transient");
    }

    public boolean hasAnnotation(String regex) {
        return annotations.exists(annotation -> annotation.matches(regex + "([(].*[)])?"));
    }

    public boolean hasModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return visible == attribute.visible &&
                Objects.equals(name, attribute.name) &&
                Objects.equals(klass, attribute.klass) &&
                Objects.equals(modifiers, attribute.modifiers) &&
                Objects.equals(annotations, attribute.annotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, klass, visible, modifiers, annotations);
    }

    public boolean isInheritable() {
        return isVisible() || hasModifier(Modifier.PACKAGE_PRIVATE) || hasModifier(Modifier.PROTECTED) || isPrimaryKey();
    }
}