package klass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<String> libraries = Arrays.asList("LocalDate", "LocalDateTime", "Date");

        return libraries.stream().anyMatch(library -> type.matches(library));
    }

    private boolean isSimpleGeneric(String type) {
        if (oneGeneric().stream().anyMatch(g -> type.matches(g))) {
            String generic = type.substring(type.indexOf('<') + 1, type.lastIndexOf('>'));

            return isIgnored(generic);
        } else if (twoGeneric().stream().anyMatch(g -> type.matches(g))) {
            String generic = type.substring(type.lastIndexOf(',') + 1, type.lastIndexOf('>')).replaceAll("\\+s", "");

            return isIgnored(generic);
        }

        return false;
    }

    private List<String> oneGeneric() {
        return Arrays.asList("List<.+>" /* listRegex */,
                "Set<.+>" /* setRegex */,
                "Optional<.*>" /*optionalRegex*/);
    }

    private List<String> twoGeneric() {
        return Arrays.asList("Map<.+\\s?,\\s?.+>" /* mapRegex */,
                "Pair<.*,.*>" /* pairRegex */);
    }

    private List<String> primitives() {
        List<String> primivites = Arrays.asList("int", "float", "double", "char", "short", "long", "boolean", "byte");
        List<String> almostPrimitives = Arrays.asList("Integer", "Float", "Double", "Character", "Short", "Long",
                "Boolean", "Byte", "String");

        List<String> result = new ArrayList<>(primivites);
        result.addAll(almostPrimitives);

        return result;
    }

    private boolean isPrimitive(String type) {
        return primitives().contains(type);
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public boolean isPrimaryKey() {
        return annotations.stream().anyMatch(annotation -> annotation.matches("@Id"));
    }
}