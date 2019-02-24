package klass;

public class Attribute {
    private String name;
    private String klass;
    private Boolean visible;

    public Attribute(String name, String klass, Boolean visible) {
        this.name = name;
        this.klass = klass;
        this.visible = visible;
    }

    public Boolean isVisible() {
        return visible;
    }

    public String getKlass() {
        return klass;
    }

    public String getName() {
        return name;
    }
}
