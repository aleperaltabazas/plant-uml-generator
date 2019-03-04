package klass.classtype;

import java.util.ArrayList;
import java.util.List;

public class AbstractKlass implements ClassType {
    public List<String> enumConstants() {
        return new ArrayList<>();
    }

    public String javaDefinition() {
        return "abstract class ";
    }
}
