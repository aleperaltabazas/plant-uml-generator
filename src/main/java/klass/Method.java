package klass;

import java.util.List;

public class Method {
    private String returnType;
    private List<Argument> arguments;
    private boolean visible;
    private String name;

    public Method(String name, String returnType, List<Argument> arguments, boolean visible) {
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

    public boolean isVisible() {
        return visible;
    }

    public String getName() {
        return name;
    }

    public boolean isGetter() {
        String getterRegex = "get\\w+";
        return name.matches(getterRegex);
    }

    public boolean isSetter() {
        String setterRegex = "set\\w+";
        return name.matches(setterRegex);
    }

    public boolean isBoilerPlate() {
        return isSetter() || isGetter();
    }
}
