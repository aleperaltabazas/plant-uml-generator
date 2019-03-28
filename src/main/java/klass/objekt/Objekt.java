package klass.objekt;

import klass.Attribute;
import klass.Klass;
import klass.Method;
import klass.Modifier;
import klass.classtype.ConcreteKlass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Objekt extends Klass {
    private static volatile Objekt instance;

    public Objekt() {
        super(new ArrayList<>(), new ArrayList<>(), "Object", new ConcreteKlass(), null, new ArrayList<>(),
                Arrays.asList(Modifier.Public), new ArrayList<>());
    }

    public static synchronized Objekt getInstance() {
        if (instance == null) instance = new Objekt();

        return instance;
    }

    @Override
    public boolean isIgnorable() {
        return false;
    }

    @Override
    public List<Method> getInheritableMethods() {
        return getMethods();
    }

    @Override
    public List<Attribute> getInheritableAttributes() {
        return getAttributes();
    }

    @Override
    public List<Method> inheritedMethods() {
        return getMethods();
    }

    @Override
    public List<Attribute> inheritedAttributes() {
        return getAttributes();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}