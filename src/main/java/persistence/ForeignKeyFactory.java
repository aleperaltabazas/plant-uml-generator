package persistence;

import exceptions.NotForeignKeyAttribute;
import klass.Attribute;
import klass.Klass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static utils.SnakeCaser.camelToSnake;

public class ForeignKeyFactory {
    public static List<ForeignKey> foreignKeysOf(Klass klass) {
        List<ForeignKey> foreignKeys = new ArrayList<>();

        List<Attribute> attributes =
                klass.getAttributes().stream().filter(Attribute::isForeignKey).collect(Collectors.toList());

        attributes.forEach(attribute -> {
            String keyName = camelToSnake(attribute.getName());
            FKType keyType = parseKeyType(attribute);
            String originTable;
            String destinationTable;

            switch (keyType) {
                case OneToOne:
                case ManyToOne:
                case ManyToMany:
                    originTable = klass.getName();
                    destinationTable = attribute.getKlass();
                    break;
                case OneToMany:
                    originTable = attribute.getKlass();
                    destinationTable = klass.getName();
                    keyType = FKType.ManyToOne;
                    break;
                default:
                    throw new NullPointerException();
            }

            foreignKeys.add(new ForeignKey(keyName, keyType, originTable, destinationTable));
        });

        return foreignKeys;
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
        throw new NotForeignKeyAttribute(attribute);
    }
}