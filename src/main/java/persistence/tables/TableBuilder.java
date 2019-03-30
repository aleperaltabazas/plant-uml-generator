package persistence.tables;

import klass.Klass;
import persistence.attributes.ForeignKey;

import java.util.List;

public interface TableBuilder {
    TableBuilder parse(Klass entity, List<Klass> others);
    TableBuilder takeForeignKeys(List<ForeignKey> foreignKeys);
    Table build();
}