package klass;

import java.util.List;

public class Method {
    private String returnType;
    private List<Argument> arguments;
    private Boolean visible;
    private String name;

    public Method(String name, String returnType, List<Argument> arguments, Boolean visible) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
        this.visible = visible;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public Boolean isVisible() {
        return visible;
    }

    public String getName() {
        return name;
    }
}
