package klass.classtype;

import io.vavr.collection.List;

import java.util.Objects;

public class EnumKlass implements ClassType {
    private List<String> enumerated;

    public EnumKlass(List<String> enumerated) {
        this.enumerated = enumerated;
    }

    public String javaDefinition() {
        return "enum ";
    }

    public String enumConstants() {
        StringBuilder sb = new StringBuilder();

        sb.append(enumerated.get(0));
        enumerated.drop(1).forEach(e -> sb.append(", ").append(e));

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnumKlass enumKlass = (EnumKlass) o;
        return Objects.equals(enumerated, enumKlass.enumerated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enumerated);
    }
}