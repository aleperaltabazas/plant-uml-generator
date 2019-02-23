package klass;

public class Attribute {
    private String name;
    private String klass;
    private boolean visible;

    public Attribute(String name, String klass, boolean visible) {
        this.name = name;
        this.klass = klass;
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getKlass() {
        return klass;
    }

    public String getName() {
        return name;
    }
}
