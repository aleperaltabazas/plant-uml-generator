package klass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Attribute {
    private String name;
    private String klass;
    private boolean visible;

    public Attribute(String name, String klass, boolean visible) {
        this.name = name;
        this.klass = klass;
        this.visible = visible;
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
        return isPrimitive(type) || isLibrary(type) || isSimpleCollection(type);
    }

    private boolean isSimpleCollection(String type) {
        String listRegex = "List<.+>";
        String setRegex = "Set<.+>";
        String mapRegex = "Map<.+\\s?,\\s?.+>";

        if (type.matches(listRegex) || type.matches(setRegex)) {
            String generic = type.substring(type.indexOf('<') + 1, type.lastIndexOf('>'));

            return isIgnored(generic);
        } else if (type.matches(mapRegex)) {
            String generic = type.substring(type.lastIndexOf(',') + 1, type.lastIndexOf('>')).replaceAll("\\+s", "");

            return isIgnored(generic);
        }

        return false;
    }

    private boolean isLibrary(String type) {
        List<String> library = Arrays.asList("Optional<.*>", "Pair<.*,.*>");

        return library.stream().anyMatch(l -> l.matches(type));
    }

    private List<String> primitives() {
        List<String> primivites = Arrays.asList("int", "float", "double", "char", "short", "long", "boolean", "byte");
        List<String> almostPrimitives = Arrays.asList("Integer", "Float", "Double", "Character", "Short", "Long", "Boolean", "Byte", "String");

        List<String> result = new ArrayList<>(primivites);
        result.addAll(almostPrimitives);

        return result;
    }

    private boolean isPrimitive(String type) {
        return primitives().contains(type);
    }
}