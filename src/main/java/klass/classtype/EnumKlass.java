package klass.classtype;

import java.util.List;

public class EnumKlass implements ClassType {
    private List<String> enumerated;

    public EnumKlass(List<String> enumerated) {
        this.enumerated = enumerated;
    }

    public String javaDefinition() {
        return null;
    }

    public List<String> enumConstants() {
        return enumerated;
    }
}
