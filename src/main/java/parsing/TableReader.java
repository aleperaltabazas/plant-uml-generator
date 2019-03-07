package parsing;

import klass.Klass;
import persistence.ForeignKey;
import persistence.Table;
import persistence.TableManager;

import java.util.ArrayList;
import java.util.List;

public class TableReader {
    public List<ForeignKey> readAllFks(List<Klass> klasses) {
        List<ForeignKey> fks = new ArrayList<>();
        klasses.forEach(k -> fks.addAll(new TableManager().readFKs(k)));

        return fks;
    }

    public List<Table> readAllTables(List<Klass> klasses) {
        List<Table> tables = new ArrayList<>();

        klasses.forEach(k -> tables.add(new TableManager().tableOf(k, readAllFks(klasses))));

        return tables;
    }
}