package utils;

import klass.Attribute;

import java.util.Arrays;

public class AttributeFactory {
    public static Attribute simple(String name, String type) {
        return new Attribute(name, type, true, Arrays.asList(), Arrays.asList());
    }

    public static Attribute oneToOne(String name, String type) {
        return withAnnotation(name, type, "@OneToOne");
    }

    public static Attribute oneToMany(String name, String type) {
        return withAnnotation(name, type, "@OneToMany");
    }


    public static Attribute manyToOne(String name, String type) {
        return withAnnotation(name, type, "@ManyToOne");
    }

    public static Attribute manyToMany(String name, String type) {
        return withAnnotation(name, type, "@ManyToMany");
    }

    private static Attribute withAnnotation(String name, String type, String annotation) {
        return new Attribute(name, type, true, Arrays.asList(), Arrays.asList(annotation));
    }

    public static Attribute pk(String name, String type) {
        return withAnnotation(name, type, "@Id");
    }
}