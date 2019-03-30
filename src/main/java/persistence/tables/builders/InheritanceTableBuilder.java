package persistence.tables.builders;

import exceptions.NoSuchStrategyException;
import klass.Klass;
import persistence.attributes.ForeignKey;
import persistence.tables.InheritanceType;
import persistence.tables.Table;

import java.util.ArrayList;
import java.util.List;

public class InheritanceTableBuilder implements TableBuilder {
    private Klass parent;
    private List<Klass> children;
    private List<ForeignKey> fks;
    private InheritanceType type;

    public InheritanceTableBuilder() {
        this.children = new ArrayList<>();
        this.fks = new ArrayList<>();
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
            this.type = InheritanceType.MAPPED;
        } else {
            throw new NoSuchStrategyException(klass);
        }
    }

    private void parseChildren(Klass klass, List<Klass> others) {
        others.stream().filter(k -> k.getSuperKlass().equals(klass)).forEach(k -> children.add(k));
    }

    public TableBuilder takeForeignKeys(List<ForeignKey> foreignKeys) {
        List<ForeignKey> fks = new ArrayList<>();

        foreignKeys.forEach(fk -> {
            if (fk.getOriginTable().equalsIgnoreCase(parent.getName())) fks.add(fk);
        });


        this.fks.addAll(fks);
        return this;
    }

    public Table build() {
        return null;
    }
}