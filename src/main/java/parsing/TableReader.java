package parsing;

import exceptions.NoPrimaryKeyError;
import klass.Klass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.ForeignKey;
import persistence.ForeignKeyFactory;
import persistence.Table;
import persistence.TableBuilder;

import java.util.ArrayList;
import java.util.List;

public class TableReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableReader.class);

    public Table readTable(Klass klass, List<ForeignKey> foreignKeys) {
        TableBuilder tb = new TableBuilder();
        tb.parse(klass);
        tb.takeForeignKeys(foreignKeys);

        return tb.build();
    }

    public List<Table> readAllTables(List<Klass> klasses, List<ForeignKey> foreignKeys) {
        List<Table> tables = new ArrayList<>();


        klasses.stream().filter(Klass::isEntity).forEach(klass -> {
            try {
                tables.add(readTable(klass, foreignKeys));
            } catch (NoPrimaryKeyError e) {
                LOGGER.error("No PK found for Class " + klass.getName(), e);
            }
        });

        return tables;
    }

    public List<ForeignKey> readAllFks(List<Klass> klasses) {
        List<ForeignKey> foreignKeys = new ArrayList<>();
        klasses.forEach(klass -> foreignKeys.addAll(ForeignKeyFactory.foreignKeysOf(klass)));

        return foreignKeys;
    }
}