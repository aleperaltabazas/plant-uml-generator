package klass.classtype;

import java.util.Objects;

public class AbstractKlass implements ClassType {
    public String enumConstants() {
        return "";
    }

    public String javaDefinition() {
        return "abstract class ";
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AbstractKlass;
    }
}
