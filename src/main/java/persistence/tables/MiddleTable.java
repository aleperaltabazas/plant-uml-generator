package persistence.tables;

import io.vavr.collection.List;
import persistence.attributes.ForeignKey;
import persistence.attributes.TableAttribute;

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
        return List.empty();
    }

    public List<ForeignKey> getFks() {
        return foreignKeys;
    }

    public String getName() {
        return tableName;
    }

    public String write() {
        return null;
    }
}
