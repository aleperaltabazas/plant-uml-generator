package klass.objekt;

import klass.Attribute;
import klass.Klass;
import klass.Method;
import klass.Modifier;
import klass.classtype.ConcreteKlass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectClass extends Klass {
    private static volatile ObjectClass instance;

    public ObjectClass() {
        super(new ArrayList<>(), ObjectMethods.all(), "Object", new ConcreteKlass(), null, new ArrayList<>(),
                Arrays.asList(Modifier.Public), new ArrayList<>());
    }

    public static synchronized ObjectClass getInstance() {
        if (instance == null) instance = new ObjectClass();

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