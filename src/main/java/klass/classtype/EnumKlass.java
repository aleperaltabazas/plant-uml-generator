package klass.classtype;

import java.util.List;

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
        enumerated.stream().skip(1).forEach(e -> sb.append(", ").append(e));

        return sb.toString();
    }
}
