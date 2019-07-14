package persistence.tables.builders;

import klass.Klass;
import persistence.attributes.ForeignKey;
import persistence.tables.Table;

import java.util.List;

public interface TableBuilder {
    TableBuilder parse(Klass entity, List<Klass> others);

    TableBuilder takeForeignKeys(List<ForeignKey> foreignKeys);

    Table build();
}