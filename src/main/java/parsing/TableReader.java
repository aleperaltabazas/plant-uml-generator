package parsing;

import klass.Klass;
import persistence.ForeignKey;
import persistence.ForeignKeyFactory;
import persistence.Table;
import persistence.TableBuilder;

import java.util.ArrayList;
import java.util.List;

public class TableReader {
    public Table readTable(Klass klass, List<ForeignKey> foreignKeys) {
        TableBuilder tb = new TableBuilder();
        tb.parse(klass);
        tb.takeForeignKeys(foreignKeys);

        return tb.build();
    }

    public List<Table> readAllTables(List<Klass> klasses, List<ForeignKey> foreignKeys) {
        List<Table> tables = new ArrayList<>();

        klasses.forEach(klass -> tables.add(readTable(klass, foreignKeys)));

        return tables;
    }

    public List<ForeignKey> readAllFks(List<Klass> klasses) {
        List<ForeignKey> foreignKeys = new ArrayList<>();
        klasses.forEach(fk -> foreignKeys.addAll(ForeignKeyFactory.foreignKeysOf(fk)));

        return foreignKeys;
    }
}