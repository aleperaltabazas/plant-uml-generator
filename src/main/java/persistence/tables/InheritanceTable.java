package persistence.tables;

import klass.Klass;
import persistence.attributes.ForeignKey;
import persistence.attributes.TableAttribute;
import persistence.tables.inheritance.InheritanceType;

import java.util.List;

public class InheritanceTable {
    private Klass parent;
    private List<Klass> children;
    private List<ForeignKey> fks;
    private List<TableAttribute> attributes;
    private InheritanceType type;
}