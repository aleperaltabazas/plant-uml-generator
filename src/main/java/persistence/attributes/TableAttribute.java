package persistence.attributes;

public class TableAttribute {
    private String name;
    private String type;

    public TableAttribute(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
