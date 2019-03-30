package persistence.tables.inheritance;

import klass.Klass;
import persistence.attributes.ForeignKey;

import java.util.List;

public class MappedSuperclass implements InheritanceType {
    public String write(Klass parent, List<Klass> children, List<ForeignKey> foreignKeys) {
        return null;
    }
}