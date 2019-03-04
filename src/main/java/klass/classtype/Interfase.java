package klass.classtype;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Interfase implements ClassType {
    public List<String> enumConstants() {
        return new ArrayList<>();
    }

    public String javaDefinition() {
        return "interface ";
    }
}
