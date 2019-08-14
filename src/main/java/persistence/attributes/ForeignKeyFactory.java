package persistence.attributes;

import io.vavr.collection.List;
import klass.Klass;

public class ForeignKeyFactory {
    public static List<ForeignKey> foreignKeysOf(Klass klass) {
        return klass.getAttributes().filter(a -> a.isForeignKey()).map(a -> ForeignKey.of(klass.getName(), a));
    }
}