package klass;

import java.util.Objects;

public class Argument {
    private String klass;
    private String name;

    public Argument(String type, String name) {
        this.klass = type;
        this.name = name;
    }

    public String getKlass() {
        return klass;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Argument argument = (Argument) o;
        return Objects.equals(klass, argument.klass) &&
                Objects.equals(name, argument.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(klass, name);
    }
}
