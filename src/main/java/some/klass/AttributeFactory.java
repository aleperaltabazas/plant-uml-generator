package some.klass;

public class AttributeFactory {
    public static Attribute getAttribute(String name, Klass klass, String file) {
        boolean visible = file.toLowerCase().contains("public" + klass.getName().toLowerCase() + "get" + name);

        return new Attribute(name, klass, visible);
    }
}
