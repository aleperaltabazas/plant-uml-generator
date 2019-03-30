package parsing;

import exceptions.NoPrimaryKeyError;
import klass.Klass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.attributes.ForeignKey;
import persistence.attributes.ForeignKeyFactory;
import persistence.tables.builders.InheritanceTableBuilder;
import persistence.tables.builders.SimpleTableBuilder;
import persistence.tables.Table;
import persistence.tables.builders.TableBuilder;

import java.util.ArrayList;
import java.util.List;

public class TableReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableReader.class);

    public Table readTable(Klass klass, List<Klass> others, List<ForeignKey> foreignKeys) {
        TableBuilder tb = klass.isInherited() ? new InheritanceTableBuilder() : new SimpleTableBuilder();
        return tb.parse(klass, others).takeForeignKeys(foreignKeys).build();
    }

    public List<Table> readAllTables(List<Klass> klasses, List<ForeignKey> foreignKeys) {
        List<Table> simpleTables = new ArrayList<>();


        klasses.stream().filter(Klass::isEntity).forEach(klass -> {
            try {
                simpleTables.add(readTable(klass, klasses, foreignKeys));
            } catch (NoPrimaryKeyError e) {
                LOGGER.error("No PK found for Class " + klass.getName(), e);
            }
        });

        return simpleTables;
    }

    public List<ForeignKey> readAllFks(List<Klass> klasses) {
        List<ForeignKey> foreignKeys = new ArrayList<>();
        klasses.forEach(klass -> foreignKeys.addAll(ForeignKeyFactory.foreignKeysOf(klass)));

        return foreignKeys;
    }
}