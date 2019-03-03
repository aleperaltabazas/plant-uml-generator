package klass;

public class Argument {
    private String klass;
    private String name;

    public Argument(String type, String name) {
        this.klass = type;
        this.name = name;
    }

    public String getKlass() {
        return klass;
    }

    public String getName() {
        return name;
    }
}
