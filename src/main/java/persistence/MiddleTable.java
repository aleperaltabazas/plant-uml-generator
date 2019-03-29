package persistence;

import java.util.ArrayList;
import java.util.List;

public class MiddleTable extends Table {
    private String tableName;

    public MiddleTable(String tableName, List<ForeignKey> foreignKeys) {
        super(null, null, new ArrayList<>(), foreignKeys);
        this.tableName = tableName;
    }

    @Override
    public String getName() {
        return tableName;
    }
}
