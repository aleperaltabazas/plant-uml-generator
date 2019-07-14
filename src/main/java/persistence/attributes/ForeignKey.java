package persistence.attributes;

import exceptions.NotAForeignKeyException;
import klass.Attribute;

import static utils.ObjectToEntity.camelToSnake;
import static utils.StringEditor.removeListWrapper;

public class ForeignKey {
    private String name;
    private FKType type;
    private String originTable;
    private String destinationTable;
    private boolean nullable;

    public ForeignKey(String name, FKType type, String originTable, String destinationTable, boolean nullable) {
        this.name = name;
        this.type = type;
        this.originTable = originTable;
        this.destinationTable = destinationTable;
        this.nullable = nullable;
    }

    public static ForeignKey of(String holderKlass, Attribute attribute) {
        if (!attribute.isForeignKey())
            throw new NotAForeignKeyException(attribute);

        String keyName = camelToSnake(attribute.getName());
        FKType keyType = parseKeyType(attribute);
        String originTable;
        String destinationTable;

        switch (keyType) {
            case OneToOne:
            case ManyToOne:
            case ManyToMany:
                originTable = holderKlass;
                destinationTable = attribute.getKlass();
                break;
            case OneToMany:
                originTable = attribute.getKlass().matches("\\w+<.*>") ? removeListWrapper(attribute.getKlass()) :
                        attribute.getKlass();
                destinationTable = holderKlass;
                keyType = FKType.ManyToOne;
                break;
            default:
                throw new NullPointerException();
        }

        //TODO: add nullable checking
        return new ForeignKey(keyName, keyType, originTable, destinationTable, false);
    }

    private static FKType parseKeyType(Attribute attribute) {
        if (attribute.hasAnnotation("@OneToOne"))
            return FKType.OneToOne;
        if (attribute.hasAnnotation("@OneToMany"))
            return FKType.OneToMany;
        if (attribute.hasAnnotation("@ManyToOne"))
            return FKType.ManyToOne;
        if (attribute.hasAnnotation("@ManyToMany"))
            return FKType.ManyToMany;
        throw new NotAForeignKeyException(attribute);
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

    public boolean isNullable() {
        return nullable;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (!(object instanceof ForeignKey)) return false;
        ForeignKey fk = (ForeignKey) object;

        return fk.name.equals(this.name) && fk.originTable.equals(this.originTable) && fk.type == this.type && fk.destinationTable.equals(this.destinationTable);
    }
}