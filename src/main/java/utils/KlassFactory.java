package utils;

import klass.Attribute;
import klass.Klass;
import klass.Modifier;
import klass.classtype.ConcreteKlass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KlassFactory {
    public static Klass withAttributes(String name, List<Attribute> attributes) {
        return withAttributesAndAnnotations(name, attributes, new ArrayList<>());
    }

    public static Klass emptyClass(String name) {
        return withAttributesAndAnnotations(name, new ArrayList<>(), new ArrayList<>());
    }

    public static Klass simpleEntity(String name, Attribute primaryKey) {
        return withAttributesAndAnnotations(name, Arrays.asList(primaryKey), Arrays.asList("@Entity"));
    }

    public static Klass entityWithAttributes(String name, List<Attribute> attributes) {
        return withAttributesAndAnnotations(name, attributes, Arrays.asList("@Entity"));
    }

    private static Klass withAttributesAndAnnotations(String name, List<Attribute> attributes,
                                                      List<String> annotations) {
        return new Klass(attributes, new ArrayList<>(), name, new ConcreteKlass(), new ArrayList<>(), null,
                Arrays.asList(Modifier.Public), annotations);
    }
}