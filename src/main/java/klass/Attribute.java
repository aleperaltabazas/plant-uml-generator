package klass;

public class Attribute {
    private String name;
    private Klass klass;
    private boolean visible;

    public Attribute(String name, Klass klass, boolean visible) {
        this.name = name;
        this.klass = klass;
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public Klass getKlass() {
        return klass;
    }

    public String getName() {
        return name;
    }
}
