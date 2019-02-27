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
        List<String> primivites = Arrays.asList("int", "float", "double", "char", "short", "long", "boolean", "byte");
        List<String> almostPrimitives = Arrays.asList("Integer", "Float", "Double", "Character", "Short", "Long", "Boolean", "Byte", "String");
        List<String> other = Arrays.asList("Optional<.*>", "Pair<.*,.*>");

        List<String> ignored = new ArrayList<>();
        ignored.addAll(primivites);
        ignored.addAll(almostPrimitives);
        ignored.addAll(other);

        for (String ignorable : ignored) {
            if (klass.matches(ignorable))
                return true;
        }

        return false;
    }
}
