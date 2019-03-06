package persistence;

import java.util.List;

public class Table {
    private String name;
    private String pk;
    private List<TableAttribute> attributes;
    private List<ForeignKey> fks;

    public Table(String name, String pk, List<TableAttribute> attributes, List<ForeignKey> fks) {
        this.name = name;
        this.pk = pk;
        this.attributes = attributes;
        this.fks = fks;
    }

    public String getName() {
        return name;
    }

    public String getPk() {
        return pk + " (PK)";
    }

    public List<TableAttribute> getAttributes() {
        return attributes;
    }

    public List<ForeignKey> getFks() {
        return fks;
    }
}