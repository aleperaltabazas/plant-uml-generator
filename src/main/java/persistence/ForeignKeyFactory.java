package persistence;

import klass.Attribute;
import klass.Klass;

import java.util.ArrayList;
import java.util.List;

public class ForeignKeyFactory {
    public static List<ForeignKey> foreignKeysOf(Klass klass) {
        System.out.println(klass.getName());
        List<ForeignKey> foreignKeys = new ArrayList<>();

        klass.getAttributes().stream().filter(Attribute::isForeignKey).forEach(attribute -> foreignKeys
                .add(ForeignKey.of(klass.getName(), attribute)));

        return foreignKeys;
    }
}