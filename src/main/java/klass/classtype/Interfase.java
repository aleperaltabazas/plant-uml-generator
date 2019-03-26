package klass.classtype;

import java.util.Objects;

public class Interfase implements ClassType {
    public String enumConstants() {
        return "";
    }

    public String javaDefinition() {
        return "interface ";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Interfase;
    }
}
