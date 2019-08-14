package parsing;

import exceptions.NoPrimaryKeyError;
import io.vavr.collection.List;
import io.vavr.control.Option;
import klass.Klass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.attributes.ForeignKey;
import persistence.attributes.ForeignKeyFactory;
import persistence.tables.Table;
import persistence.tables.builders.InheritanceTableBuilder;
import persistence.tables.builders.SimpleTableBuilder;
import persistence.tables.builders.TableBuilder;

public class TableReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableReader.class);

    public Table readTable(Klass klass, List<Klass> others, List<ForeignKey> foreignKeys) {
        TableBuilder tb = klass.isInherited() ? new InheritanceTableBuilder() : new SimpleTableBuilder();
        return tb.parse(klass, others).takeForeignKeys(foreignKeys).build();
    }

    public List<Table> readAllTables(List<Klass> klasses, List<ForeignKey> foreignKeys) {
        return klasses.filter(k -> k.isEntity())
                .map(k -> {
                    try {
                        return Option.of(readTable(k, klasses, foreignKeys));
                    } catch (NoPrimaryKeyError e) {
                        LOGGER.error("No PK found for Class " + k.getName(), e);
                        return Option.none();
                    }
                })
                .flatMap(opt -> (List<Table>) opt.toList());
    }

    public List<ForeignKey> readAllFks(List<Klass> klasses) {
        return klasses.flatMap(klass -> ForeignKeyFactory.foreignKeysOf(klass));
    }
}