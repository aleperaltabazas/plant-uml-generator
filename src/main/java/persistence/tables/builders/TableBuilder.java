package persistence.tables.builders;

import io.vavr.collection.List;
import klass.Klass;
import persistence.attributes.ForeignKey;
import persistence.tables.Table;

public interface TableBuilder {
    TableBuilder parse(Klass entity, List<Klass> others);

    TableBuilder takeForeignKeys(List<ForeignKey> foreignKeys);

    Table build();
}