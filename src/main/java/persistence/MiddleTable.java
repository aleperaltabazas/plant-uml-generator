package persistence;

import java.util.ArrayList;
import java.util.List;

public class MiddleTable implements Table {
    private String tableName;
    private List<ForeignKey> foreignKeys;

    public MiddleTable(String tableName, List<ForeignKey> foreignKeys) {
        this.tableName = tableName;
        this.foreignKeys = foreignKeys;
    }

    public String getPk() {
        return null;
    }

    public List<TableAttribute> getAttributes() {
        return new ArrayList<>();
    }

    public List<ForeignKey> getFks() {
        return foreignKeys;
    }

    public String getName() {
        return tableName;
    }
}
