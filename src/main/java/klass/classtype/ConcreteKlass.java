package klass.classtype;

import java.util.Objects;

public class ConcreteKlass implements ClassType {
    public String enumConstants() {
        return "";
    }

    public String javaDefinition() {
        return "class ";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ConcreteKlass;
    }
}