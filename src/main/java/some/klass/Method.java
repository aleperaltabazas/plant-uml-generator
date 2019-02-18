package some.klass;

import java.util.List;

public class Method {
    private Klass returnType;
    private List<Argument> arguments;
    private boolean visible;

    public Method(Klass returnType, List<Argument> arguments, boolean visible) {
        this.returnType = returnType;
        this.arguments = arguments;
        this.visible = visible;
    }

    public Klass getReturnType() {
        return returnType;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public boolean isVisible() {
        return visible;
    }
}
