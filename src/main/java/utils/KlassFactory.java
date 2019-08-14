package utils;

import io.vavr.collection.List;
import klass.Attribute;
import klass.Klass;
import klass.Modifier;
import klass.classtype.ConcreteKlass;
import klass.objekt.ObjectClass;

public class KlassFactory {
    public static Klass withAttributes(String name, List<Attribute> attributes) {
        return withAttributesAndAnnotations(name, attributes, List.empty());
    }

    public static Klass emptyClass(String name) {
        return withAttributesAndAnnotations(name, List.empty(), List.empty());
    }

    public static Klass simpleEntity(String name, Attribute primaryKey) {
        return withAttributesAndAnnotations(name, List.of(primaryKey), List.of("@Entity"));
    }

    public static Klass entityWithAttributes(String name, List<Attribute> attributes) {
        return withAttributesAndAnnotations(name, attributes, List.of("@Entity"));
    }

    private static Klass withAttributesAndAnnotations(String name, List<Attribute> attributes,
                                                      List<String> annotations) {
        return new Klass(attributes, List.empty(), name, new ConcreteKlass(), ObjectClass.getInstance(),
                List.empty(), List.of(Modifier.PUBLIC), annotations);
    }
}