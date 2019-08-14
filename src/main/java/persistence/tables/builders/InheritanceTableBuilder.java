package persistence.tables.builders;

import exceptions.NoSuchStrategyException;
import io.vavr.collection.List;
import klass.Klass;
import persistence.attributes.ForeignKey;
import persistence.tables.Table;
import persistence.tables.inheritance.InheritanceType;
import persistence.tables.inheritance.MappedSuperclass;


public class InheritanceTableBuilder implements TableBuilder {
    private Klass parent;
    private List<Klass> children;
    private List<ForeignKey> fks;
    private InheritanceType type;

    public InheritanceTableBuilder() {
        this.children = List.empty();
        this.fks = List.empty();
    }

    public TableBuilder parse(Klass klass, List<Klass> others) {
        parent = klass;
        parseChildren(klass, others);
        parseType(klass);
        return this;
    }

    private void parseType(Klass klass) {
        if (klass.inheritanceStrategy()) {
            this.type = InheritanceType.parse(klass.getInheritanceStrategy());
        } else if (klass.mappedSuperclass()) {
            this.type = new MappedSuperclass();
        } else {
            throw new NoSuchStrategyException(klass);
        }
    }

    private void parseChildren(Klass klass, List<Klass> others) {
        children = children.appendAll(others.filter(k -> k.getSuperKlass().equals(klass)));
    }

    public TableBuilder takeForeignKeys(List<ForeignKey> foreignKeys) {
        //TODO: also children classes
        List<ForeignKey> fks = foreignKeys.filter(fk -> fk.getOriginTable().equalsIgnoreCase(parent.getName()));

        this.fks = this.fks.appendAll(fks);
        return this;
    }

    public Table build() {
        //TODO
        return null;
    }
}