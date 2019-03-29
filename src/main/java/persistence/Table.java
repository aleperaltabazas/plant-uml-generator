package persistence;

import klass.Klass;
import klass.objekt.ObjectClass;

import java.util.List;

import static utils.ObjectToEntity.camelToSnake;

public class Table {
    private Klass klass;
    private String pk;
    private List<TableAttribute> attributes;
    private List<ForeignKey> fks;

    public Table(Klass klass, String pk, List<TableAttribute> attributes, List<ForeignKey> fks) {
        this.klass = klass;
        this.pk = pk;
        this.attributes = attributes;
        this.fks = fks;
    }

    public boolean isInherited() {
        return klass.getSuperKlass() != ObjectClass.getInstance();
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