package persistence.tables;

import io.vavr.collection.List;
import klass.Klass;
import persistence.attributes.ForeignKey;
import persistence.attributes.TableAttribute;

import static utils.ObjectToEntity.camelToSnake;

public class RegularTable implements Table {
    private Klass klass;
    private String pk;
    private List<TableAttribute> attributes;
    private List<ForeignKey> fks;

    public RegularTable(Klass klass, String pk, List<TableAttribute> attributes, List<ForeignKey> fks) {
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

    public String write() {
        return null;
    }
}