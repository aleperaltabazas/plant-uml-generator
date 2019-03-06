package persistence;

import klass.Klass;

public class ForeignKey {
    private String name;
    private FKType type;
    private Klass originTable;
    private Klass destinationTable;

    public String getName() {
        return name + " (FK)";
    }

    public FKType getType() {
        return type;
    }

    public Klass getOriginTable() {
        return originTable;
    }

    public Klass getDestinationTable() {
        return destinationTable;
    }
}