package persistence;

public class ForeignKey {
    private String name;
    private FKType type;
    private String originTable;
    private String destinationTable;

    public ForeignKey(String name, FKType type, String originTable, String destinationTable) {
        this.name = name;
        this.type = type;
        this.originTable = originTable;
        this.destinationTable = destinationTable;
    }

    public String getName() {
        return name + " (FK)";
    }

    public FKType getType() {
        return type;
    }

    public String getOriginTable() {
        return originTable;
    }

    public String getDestinationTable() {
        return destinationTable;
    }
}