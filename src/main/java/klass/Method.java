package klass;

import java.util.Arrays;
import java.util.List;

public class Method {
    private String returnType;
    private List<Argument> arguments;
    private boolean visible;
    private String name;
    private List<Modifier> modifiers;

    public Method(String name, String returnType, List<Argument> arguments, boolean visible, List<Modifier> modifiers) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
        this.visible = visible;
        this.modifiers = modifiers;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public boolean isVisible() {
        return visible || hasModifier(Modifier.Default);
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
        return isSetter() || isGetter() || redefinition();
    }

    private boolean redefinition() {
        List<String> redefinables = Arrays.asList("equals", "hashCode", "clone");
        return redefinables.contains(name);
    }

    public boolean hasModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }
}
