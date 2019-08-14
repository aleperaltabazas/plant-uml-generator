package persistence.tables;

import io.vavr.collection.List;
import klass.Klass;
import persistence.attributes.ForeignKey;
import persistence.attributes.TableAttribute;
import persistence.tables.inheritance.InheritanceType;

public class InheritanceTable implements Table {
    private Klass parent;
    private List<Klass> children;
    private InheritanceType type;

    public String getName() {
        return null;
    }

    public String getPk() {
        return null;
    }

    public List<TableAttribute> getAttributes() {
        return null;
    }

    public List<ForeignKey> getFks() {
        return null;
    }

    public String write() {
        //return type.write(parent, children);
        return "";
    }
}