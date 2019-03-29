package persistence;

import klass.Klass;

import java.util.List;

import static utils.ObjectToEntity.camelToSnake;

public class SimpleTable implements Table {
    private Klass klass;
    private String pk;
    private List<TableAttribute> attributes;
    private List<ForeignKey> fks;

    public SimpleTable(Klass klass, String pk, List<TableAttribute> attributes, List<ForeignKey> fks) {
        this.klass = klass;
        this.pk = pk;
        this.attributes = attributes;
        this.fks = fks;
    }

    public String getName() {
        camelToSnake(klass.getName());
        return camelToSnake(klass.getName());
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